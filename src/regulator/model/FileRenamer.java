package regulator.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 31.01.2018.
 */
public class FileRenamer
{
    /*destination folder*/
    private static String sourcePath;

    /* contains four numbers of year in brackets ( (1972), (1985) , (2012) )*/
    private static String postfix;

    /*busy numbers of files*/
    private static List<Integer> busyPositions;

    /*list of unsuccess file renames*/
    private static List<String> fallingRenames;

    /*list of success file renames*/
    private static List<String> successRenames;

    /*list of files with satisfy names */
    private static List<String> satisfyNames;

    /*list of files with unsatisfied names*/
    private static List<String> unSatisfyNames;

    /*return second prefix of file, must contains number of files order (01, 36, 125)
    * return 0 if no fit*/
    private static int getPrefix(String fileName){
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
    private static String getPostfix(String filename){
        String result = filename.substring(0,filename.lastIndexOf('.'));
        return result.substring(findLastSpace(result) + 1, result.length());
    }

    /*gets position of first letter in string
    * return 0 if no letters in string*/
    private static int findFirstLetter(String string)
    {
        char[] sequence = string.toCharArray();
        for (int i = 0; i < sequence.length;i++){
            if (Character.isLetter(sequence[i])) return i;
        }
        return 0;
    }

    /*gets position of first space in string
   * return 0 if no letters in string*/
    private static int findFirstSpace(String string)
    {
        char[] sequence = string.toCharArray();
        for (int i = 0; i < sequence.length;i++){
            if (Character.isSpaceChar(sequence[i])) return i;
        }
        return 0;
    }

    /*gets position of last letter in string
   * return length-1 of string if no letters in string*/
    private static int findLastLetter(String string)
    {
        char[] sequence = string.toCharArray();
        for (int i = sequence.length-1; i >= 0;i--){
            if (Character.isLetter(sequence[i])) return i;
        }
        return string.length()-1;
    }

    /*gets position of last spase in string
  * return length-1 of string if no letters in string*/
    private static int findLastSpace(String string)
    {
        char[] sequence = string.toCharArray();
        for (int i = sequence.length-1; i >= 0;i--){
            if (Character.isSpaceChar(sequence[i])) return i;
        }
        return string.length()-1;
    }

    /*fill static field postfix */
    private static void fillPostfix(){
        String result = sourcePath.substring(sourcePath.lastIndexOf('\\')+1);
        postfix = "(" + result + ")";
    }

    /*rename file to new name*/
    private static boolean renameFile(String oldFileName, String newFileName) throws IOException
    {
        File file = new File(sourcePath + "\\" + oldFileName);
        File file2 = new File(sourcePath + "\\" + newFileName);
        if (file2.exists())
            throw new java.io.IOException("file exists");
        return file.renameTo(file2);
    }


    /*gets old file name and create new file name*/
    private static String getNewFileName(String oldFileName)
    {
        String result = null;
        String extension = oldFileName.substring(oldFileName.lastIndexOf('.'));
        String name = oldFileName.substring(0,oldFileName.lastIndexOf('.'));
        name = name.substring(findFirstLetter(name));
        int prefix = createNewPrefix();
        if (getPostfix(oldFileName).equals(postfix)){
            result = String.format("%02d %s%s",prefix, name, extension);
        }else {
            result = String.format("%02d %s %s%s",prefix, name, postfix, extension);
        }
        return result;
    }

    /*create new prefix of file. */
    private static int createNewPrefix()
    {
        int counter = 1;
        while (busyPositions.contains(counter)){
            counter++;
        }
        return counter;
    }

    public static void main(String[] args)
    {
//        sourcePath = "D:\\MUSIC\\01\\Старинные вальсы";
        sourcePath = "D:\\MUSIC\\01\\1985";
        busyPositions = new ArrayList<>();
        fallingRenames = new ArrayList<>();
        successRenames = new ArrayList<>();
        satisfyNames = new ArrayList<>();
        unSatisfyNames = new ArrayList<>();
        fillPostfix();
        checkAllFiles(getFittingFiles(new File(sourcePath).list()));
        renameAllFiles(unSatisfyNames);
        System.out.printf("\r\n%s%d%s","*** analyzed ", (satisfyNames.size()+ unSatisfyNames.size()), " files ***");
        System.out.printf("\r\n%s%d%s", "*** renames ", successRenames.size(), " files ***");
        for (String fileName : successRenames){
            System.out.printf("\r\n%s",fileName);
        }
        System.out.printf("\r\n%s%d%s", "*** refuse renames " , fallingRenames.size(), " files ***");
        for (String fileName : fallingRenames){
            System.out.printf("\r\n%s%s%s", sourcePath, "\\", fileName);
        }
        System.out.printf("\r\n%s%d", "*** satisfy files and so no renames ", satisfyNames.size());
        for (String fileName : satisfyNames){
            System.out.printf("\r\n%s%s%s", sourcePath, "\\", fileName);
        }
    }


    /*first pass of files list, find satisfied and unsatisfied file names*/
    private static void checkAllFiles(List<String> sourceFiles){
        for (String fileName : sourceFiles){
            if (checkFileName(fileName)){
                satisfyNames.add( fileName);
                busyPositions.add(getPrefix(fileName));
            }else {
                unSatisfyNames.add(fileName);
            }
        }
    }

    /*second pass of files list, rename unsatisfied names*/
    private static void renameAllFiles(List<String> sourceFiles){
        for (String fileName : sourceFiles){
            String newFileName = getNewFileName(fileName);
            try
            {
                if (renameFile(fileName,newFileName))
                {
                    busyPositions.add(getPrefix(newFileName));
                    successRenames.add(String.format("%s%s%s %s %s%s%s",sourcePath, "\\" ,fileName,"==>" , sourcePath, "\\" ,newFileName));
                }
            }
            catch (IOException e)
            {
                fallingRenames.add(String.format("%s%s%s",sourcePath, "\\" ,fileName));
            }
        }
    }

    /*check that filename has required pre- and post-fix*/
    private static boolean checkFileName(String fileName)
    {
        int prefix = getPrefix(fileName);
        String postFix = getPostfix(fileName);
        return  ((prefix > 0)&&(postFix.equals(postfix)));
    }



    /*extract files that fit to extension*/
    private static List<String> getFittingFiles(String[] filelist){
        List<String> result = new ArrayList<>();
        for (String file : filelist){
            if (file.substring(file.lastIndexOf("."),file.length()).equalsIgnoreCase(".mp3")){
                result.add(file);
            }
        }
        return result;
    }

}
