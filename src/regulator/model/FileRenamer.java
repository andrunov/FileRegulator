package regulator.model;

import regulator.util.FileFilter;
import regulator.util.Writer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Admin on 31.01.2018.
 */
public class FileRenamer
{

    /*Localization*/
    private ResourceBundle resourceBundle;

    /*destination folder*/
    private String sourcePath;

    /* contains four numbers of year in brackets ( (1972), (1985) , (2012) )*/
    private String postfix;

    /*busy numbers of files*/
    private List<Integer> busyPositions;

    /*list of unsuccess file renames*/
    private List<String> fallingRenames;

    /*list of success file renames*/
    private List<String> successRenames;

    /*list of files with satisfy names */
    private List<String> satisfyNames;

    /*list of files with unsatisfied names*/
    private List<String> unSatisfyNames;

    /*filter of file types*/
    private FileFilter filter;

    /*constructor*/
    public FileRenamer(String sourcePath, String[] extensions, ResourceBundle resourceBundle) {
        this.sourcePath = sourcePath;
        this.busyPositions = new ArrayList<>();
        this.fallingRenames = new ArrayList<>();
        this.successRenames = new ArrayList<>();
        this.satisfyNames = new ArrayList<>();
        this.unSatisfyNames = new ArrayList<>();
        this.createPostfix();
        this.filter = new FileFilter(extensions);
        this.resourceBundle = resourceBundle;
    }

    /*return second prefix of file, must contains number of files order (01, 36, 125)
            * return 0 if no fit*/
    private int getPrefix(String fileName){
        int result = 0;
        try
        {
            int lastIndex = findFirstSpace(fileName);
            if (lastIndex==0){
                lastIndex= findFirstLetter(fileName)-1;
                lastIndex = lastIndex >=0 ? lastIndex : 0;
            }
            result = Integer.parseInt(fileName.substring(0, lastIndex));
        }
        catch (NumberFormatException e)
        {
//            e.printStackTrace();
        }
        return result ;
    }

    /*get fileName's postfix*/
    private String getPostfix(String filename){
        String result = filename.substring(0,filename.lastIndexOf('.'));
        return result.substring(findLastSpace(result) + 1, result.length());
    }

    /*gets position of first letter in string
    * return 0 if no letters in string*/
    private int findFirstLetter(String string)
    {
        char[] sequence = string.toCharArray();
        for (int i = 0; i < sequence.length;i++){
            if (Character.isLetter(sequence[i])) return i;
        }
        return 0;
    }

    /*gets position of first space in string
   * return 0 if no letters in string*/
    private int findFirstSpace(String string)
    {
        char[] sequence = string.toCharArray();
        for (int i = 0; i < sequence.length;i++){
            if (Character.isSpaceChar(sequence[i])) return i;
        }
        return 0;
    }

    /*gets position of last letter in string
   * return length-1 of string if no letters in string*/
    private int findLastLetter(String string)
    {
        char[] sequence = string.toCharArray();
        for (int i = sequence.length-1; i >= 0;i--){
            if (Character.isLetter(sequence[i])) return i;
        }
        return string.length()-1;
    }

    /*gets position of last spase in string
  * return length-1 of string if no letters in string*/
    private int findLastSpace(String string)
    {
        char[] sequence = string.toCharArray();
        for (int i = sequence.length-1; i >= 0;i--){
            if (Character.isSpaceChar(sequence[i])) return i;
        }
        return string.length()-1;
    }

    /*fill field postfix */
    private void createPostfix(){
        String result = this.sourcePath.substring(this.sourcePath.lastIndexOf('\\')+1);
        try {
            Integer.parseInt(result);
            this.postfix = "(" + result + ")";
        }
        catch (NumberFormatException e){
            this.postfix = null;
        }
    }

    /*rename file to new name*/
    private boolean renameFile(String oldFileName, String newFileName) throws IOException
    {
        File file = new File(this.sourcePath + "\\" + oldFileName);
        File file2 = new File(this.sourcePath + "\\" + newFileName);
        if (file2.exists())
            throw new java.io.IOException("file exists");
        return file.renameTo(file2);
    }


    /*gets old file name and create new file name*/
    private String getNewFileName(String oldFileName)
    {
        String result;
        String extension = oldFileName.substring(oldFileName.lastIndexOf('.'));
        String name = oldFileName.substring(0,oldFileName.lastIndexOf('.'));
        name = name.substring(findFirstLetter(name));
        int prefix = createNewPrefix();
        if ((this.postfix == null)||(getPostfix(oldFileName).equals(this.postfix))){
            result = String.format("%02d %s%s",prefix, name, extension);
        }else {
            result = String.format("%02d %s %s%s",prefix, name, this.postfix, extension);
        }
        return result;
    }

    /*create new prefix of file. */
    private int createNewPrefix()
    {
        int counter = 1;
        while (this.busyPositions.contains(counter)){
            counter++;
        }
        return counter;
    }

    /* method for start program logic from outside */
    public void execute(String reportPath){
        Writer writer = new Writer("UTF8",reportPath);
        writer.write(this.renameCycle().toString(), this.resourceBundle);
    }

    /*main cycle of program. Recursive select, check and renames all files in selected folders*/
    private StringBuilder renameCycle(){
    StringBuilder report = new StringBuilder();
        for (String fileName : new File(this.sourcePath).list()){
            String absPath = this.sourcePath + "\\" + fileName;
            if (new File(absPath).isDirectory()){
                report.append(new FileRenamer(absPath, this.filter.getExtensions(),this.resourceBundle).renameCycle());
            }
            else if (checkFileName(fileName)){
                this.satisfyNames.add( fileName);
                this.busyPositions.add(getPrefix(fileName));
            }else {
                this.unSatisfyNames.add(fileName);
            }
        }
        renameAllFiles();
        report.append(writeResult()).append("\r\n");
        return report;
    }


    /*second pass of files list, rename unsatisfied names*/
    private void renameAllFiles(){
        for (String fileName : this.unSatisfyNames){
            String newFileName = getNewFileName(fileName);
            try
            {
                if (renameFile(fileName,newFileName))
                {
                    this.busyPositions.add(getPrefix(newFileName));
                    this.successRenames.add(String.format("%s %s %s",fileName,"==>" , newFileName));
                }
            }
            catch (IOException e)
            {
                this.fallingRenames.add(fileName);
            }
        }
    }

    /*check that filename has required pre- and post-fix*/
    private boolean checkFileName(String fileName)
    {
        boolean result;
        int prefix = getPrefix(fileName);
        result = ((prefix > 0)&&(!this.busyPositions.contains(prefix)));
        if (this.postfix != null){
            String postFix = getPostfix(fileName);
            result =  result&&(postFix.equals(this.postfix));
        }
        return result;
    }

    /*write result of rename files */
    private StringBuilder writeResult(){
        StringBuilder sb = new StringBuilder("***********************************************************************************************************");
        sb.append(String.format("\r\n%-2s%-100.100s%5s","*",(this.resourceBundle.getString("Folder") +" : " + this.sourcePath),"*"));
        sb.append(String.format("\r\n%-2s%-100.100s%5s","*",(this.resourceBundle.getString("Analyzed") + " : " + (this.satisfyNames.size()+ this.unSatisfyNames.size()) + " : " + this.resourceBundle.getString("files")),"*"));
        sb.append(String.format("\r\n%-2s%-100.100s%5s","*",(this.resourceBundle.getString("Renamed") +" : " + this.successRenames.size() + " : " + this.resourceBundle.getString("files")),"*"));
        for (String fileName : this.successRenames){
            sb.append(String.format("\r\n%-5s%-100.100s%2s","+",fileName, "*"));
        }
        sb.append(String.format("\r\n%-2s%-100.100s%5s","*",(this.resourceBundle.getString("Refuse") +" : " + this.fallingRenames.size() + " : " + this.resourceBundle.getString("files")),"*"));
        for (String fileName : this.fallingRenames){
            sb.append(String.format("\r\n%-5s%-100.100s%2s","+",fileName, "*"));
        }
        sb.append(String.format("\r\n%-2s%-100.100s%5s","*",(this.resourceBundle.getString("Satysfied") +" : " + this.satisfyNames.size() + " : " + this.resourceBundle.getString("files")),"*"));
//        for (String fileName : this.satisfyNames){
//            sb.append(String.format("\r\n%-5s%-100.100s%2s","+",fileName, "*"));
//        }
        sb.append("\r\n***********************************************************************************************************");
        sb.append("\r\n");
        return sb;
    }

}
