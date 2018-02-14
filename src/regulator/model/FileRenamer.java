package regulator.model;

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

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /*constructor*/
    public FileRenamer(String sourcePath) {
        this.sourcePath = sourcePath;
        this.busyPositions = new ArrayList<>();
        this.fallingRenames = new ArrayList<>();
        this.successRenames = new ArrayList<>();
        this.satisfyNames = new ArrayList<>();
        this.unSatisfyNames = new ArrayList<>();
        this.createPostfix();
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

    /*fill static field postfix */
    private void createPostfix(){
        String result = this.sourcePath.substring(this.sourcePath.lastIndexOf('\\')+1);
        this.postfix = "(" + result + ")";
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
        String result = null;
        String extension = oldFileName.substring(oldFileName.lastIndexOf('.'));
        String name = oldFileName.substring(0,oldFileName.lastIndexOf('.'));
        name = name.substring(findFirstLetter(name));
        int prefix = createNewPrefix();
        if (getPostfix(oldFileName).equals(this.postfix)){
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

    public static void main(String[] args)    {
//        sourcePath = "D:\\MUSIC\\01\\Старинные вальсы";
        System.out.println(renameCycle("D:\\MUSIC\\01").toString());

    }


    /*main cycle of program. Recursive select, check and renames all files in selected folders*/
    private static StringBuilder renameCycle(String parentPath){
    StringBuilder report = new StringBuilder();
        FileRenamer renamer = new FileRenamer(parentPath);
        for (String fileName : new File(parentPath).list()){
            String absPath = parentPath + "\\" + fileName;
            if (new File(absPath).isDirectory()){
                report.append(renameCycle(absPath));
            }
            else if (renamer.checkFileName(fileName)){
                renamer.satisfyNames.add( fileName);
                renamer.busyPositions.add(renamer.getPrefix(fileName));
            }else {
                renamer.unSatisfyNames.add(fileName);
            }
        }
        renamer.renameAllFiles();
        report.append(renamer.writeResult()).append("\r\n");
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
                    this.successRenames.add(String.format("%s%s%s %s %s%s%s",this.sourcePath, "\\" ,fileName,"==>" , this.sourcePath, "\\" ,newFileName));
                }
            }
            catch (IOException e)
            {
                this.fallingRenames.add(String.format("%s%s%s",this.sourcePath, "\\" ,fileName));
            }
        }
    }

    /*check that filename has required pre- and post-fix*/
    private boolean checkFileName(String fileName)
    {
        int prefix = getPrefix(fileName);
        String postFix = getPostfix(fileName);
        return  ((prefix > 0)&&(postFix.equals(this.postfix))&&(!this.busyPositions.contains(prefix)));
    }

    /*write result of rename files */
    private StringBuilder writeResult(){
        StringBuilder sb = new StringBuilder("Folder: ").append(this.sourcePath);
        sb.append(String.format("\r\n%s%d%s","*** analyzed ", (this.satisfyNames.size()+ this.unSatisfyNames.size()), " files ***"));
        sb.append(String.format("\r\n%s%d%s", "*** renames ", this.successRenames.size(), " files ***"));
        for (String fileName : this.successRenames){
            sb.append(String.format("\r\n%s",fileName));
        }
        sb.append(String.format("\r\n%s%d%s", "*** refuse renames " , this.fallingRenames.size(), " files ***"));
        for (String fileName : this.fallingRenames){
            sb.append(String.format("\r\n%s%s%s", this.sourcePath, "\\", fileName));
        }
        sb.append(String.format("\r\n%s%d", "*** satisfy files and so no renames ", this.satisfyNames.size()));
        for (String fileName : this.satisfyNames){
            sb.append(String.format("\r\n%s%s%s", this.sourcePath, "\\", fileName));
        }
        sb.append("\r\n");
        return sb;
    }


}
