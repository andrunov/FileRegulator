package regulator.util;

import regulator.model.FileComparer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Class for output strings info
 */
public class Writer {

    /*link to fileComparer*/
    private FileComparer comparer;

    /*encoding*/
    private String encoding;

    /*constructor*/
    public Writer(FileComparer comparer, String encoding) {
        this.comparer = comparer;
        this.encoding = encoding;
    }

    /*write logic*/
    public boolean write(){
        boolean result = false;
        ResourceBundle resourceBundle = this.comparer.getResourceBundle();
        try{
            PrintWriter writer = new PrintWriter(comparer.getReportName(), "UTF-8");
            /*condition for single directory comparing*/
            if (!this.comparer.getStartDirectoryName().equals(this.comparer.getEndDirectoryName()))
            {
                printHeadTwoDirectory(writer);
                printSchemeTwoDirectory(writer);

                /*1-st level - 100 equality*/
                printTitle(writer,resourceBundle.getString("1stLevelEquality"));
                printFileList(writer,this.comparer.getFullEquality());

                /*2 level - 100% names equality*/
                printTitle(writer,resourceBundle.getString("2ndLevelEquality"));
                printFileList(writer,this.comparer.getNameEquality());
            }else {
                printHeadSingleDirectory(writer);
                printSchemeSingleDirectory(writer);
            }

            /*3 level - 100% sizes equality*/
            printTitle(writer,resourceBundle.getString("3thLevelEquality"));
            printFileList(writer,this.comparer.getSizeEquality());

            /*4 level - equality of 3 words and more*/
            printTitle(writer,resourceBundle.getString("4thLevelEquality"));
            printFileList(writer,this.comparer.getNameSimilarityHigh());

            /*5 level - equality from 1 to 2 words*/
            printTitle(writer,resourceBundle.getString("5thLevelEquality"));
            printFileList(writer,this.comparer.getNameSimilarityLow());

            /*6 level - no equalities
            * in this point in this.startDirectory is only filesInfo that no has similarities */
            printTitle(writer,resourceBundle.getString("6thLevelEquality"));
            printFileList(writer,this.comparer.getNoSimilarity());
            writer.close();
            result = true;
        } catch (IOException e) {
            Message.errorAlert(resourceBundle,e);
        }
        return result;
    }



    /*head for single-directory case*/
    private void printHeadSingleDirectory(PrintWriter writer) {
        ResourceBundle resourceBundle = this.comparer.getResourceBundle();
        String title = resourceBundle.getString("Analyzed")
                + " " + this.comparer.getStartDirectory().size()
                + " " + resourceBundle.getString("Files")
                + " " + resourceBundle.getString("InDirectory") + ": ";
        writer.println("***********************************************************************************************************");
        writer.printf("%-5s%-100.100s%2s","*",title,"*");
        List<String> list = Formatter.splitStringInRows(this.comparer.getStartDirectoryName(),100);
        for (String s : list){
            writer.printf("\r\n%-5s%-100.100s%2s","*",s,"*");
        }
        writer.printf("\r\n%-2s%-100.100s%5s","*","","*");
        writer.println("\r\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        list = Formatter.splitStringInRows(this.comparer.getReportName(),100);
        writer.printf("%-5s%-100.100s%2s","*",resourceBundle.getString("ReportSaveIn"),"*");
        for (String s : list){
            writer.printf("\r\n%-5s%-100.100s%2s","*",s,"*");
        }
        writer.println("\r\n***********************************************************************************************************");
    }

    /*head for two directory case*/
    private void printHeadTwoDirectory(PrintWriter writer) {
        ResourceBundle resourceBundle = this.comparer.getResourceBundle();
        String title1 = resourceBundle.getString("Analyzed")
                + " " + this.comparer.getStartDirectory().size()
                + " " + resourceBundle.getString("Files")
                + " " + resourceBundle.getString("InDirectory") + ": ";
        writer.println("***********************************************************************************************************");
        writer.printf("%-5s%-100.100s%2s","*",title1,"*");
        List<String> list = Formatter.splitStringInRows(this.comparer.getStartDirectoryName(),100);
        for (String s : list){
            writer.printf("\r\n%-5s%-100.100s%2s","*",s,"*");
        }
        writer.printf("\r\n%-2s%-100.100s%5s","*","","*");
        String title2 = resourceBundle.getString("Analyzed")
                + " " + this.comparer.getEndDirectory().size()
                + " " + resourceBundle.getString("Files")
                + " " + resourceBundle.getString("InDirectory");
        writer.printf("\r\n%-5s%-100.100s%2s","*",title2,"*");
        list = Formatter.splitStringInRows(this.comparer.getEndDirectoryName(),100);
        for (String s : list){
            writer.printf("\r\n%-5s%-100.100s%2s","*",s,"*");
        }
        writer.printf("\r\n%-2s%-100.100s%5s","*","","*");
        writer.println("\r\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        list = Formatter.splitStringInRows(this.comparer.getReportName(),100);
        writer.printf("%-5s%-100.100s%2s","*",resourceBundle.getString("ReportSaveIn"),"*");
        for (String s : list){
            writer.printf("\r\n%-5s%-100.100s%2s","*",s,"*");
        }
        writer.println("\r\n***********************************************************************************************************");
    }

    /*print schema for single-directory case*/
    private void printSchemeSingleDirectory(PrintWriter writer){
        ResourceBundle resourceBundle = this.comparer.getResourceBundle();
        writer.print(" ---------------------------------------------------------------------------------------------------------");
        writer.printf("\r\n%-2s%-103s%2s", "|", resourceBundle.getString("Schema"),"|");
        writer.printf("\r\n%-5s%102s", "|", "|");
        writer.printf("\r\n%-2s%-87.87s%9.9s%1s%3s%5s","|",resourceBundle.getString("ComparingFileSingle"),resourceBundle.getString("FileSize"),",", "mb","|");
        writer.printf("\r\n%-5s%102s", "|", "|");
        writer.printf("\r\n%-5s%-87.87s%9.9s%1s%3s%2s","|",resourceBundle.getString("SimilarFileSingle") + " №1",resourceBundle.getString("FileSize"),",", "mb","|");
        writer.printf("\r\n%-5s%-87.87s%9.9s%1s%3s%2s","|",resourceBundle.getString("SimilarFileSingle") + " №2",resourceBundle.getString("FileSize"),",", "mb","|");
        writer.printf("\r\n%-5s%-87.87s%9.9s%1s%3s%2s","|",resourceBundle.getString("SimilarFileSingle") + " №3",resourceBundle.getString("FileSize"),",", "mb","|");
        writer.print("\r\n ---------------------------------------------------------------------------------------------------------");
    }

    /*print schema for two-directory case*/
    private void printSchemeTwoDirectory(PrintWriter writer) {
        ResourceBundle resourceBundle = this.comparer.getResourceBundle();
        writer.print(" ---------------------------------------------------------------------------------------------------------");
        writer.printf("\r\n%-2s%-103s%2s", "|", resourceBundle.getString("Schema"),"|");
        writer.printf("\r\n%-5s%102s", "|", "|");
        writer.printf("\r\n%-2s%-87.87s%9.9s%1s%3s%5s"
                ,"|"
                ,resourceBundle.getString("ComparingFileSingle") + " (" + Formatter.getShortFilePath(this.comparer.getStartDirectoryName()) + ")"
                ,resourceBundle.getString("FileSize")
                ,","
                , "mb"
                ,"|");
        writer.printf("\r\n%-5s%102s", "|", "|");
        writer.printf("\r\n%-5s%-87.87s%9.9s%1s%3s%2s"
                ,"|"
                ,resourceBundle.getString("SimilarFileSingle") + " №1 (" + Formatter.getShortFilePath(this.comparer.getEndDirectoryName()) + ")"
                ,resourceBundle.getString("FileSize")
                ,","
                , "mb"
                ,"|");
        writer.printf("\r\n%-5s%-87.87s%9.9s%1s%3s%2s"
                ,"|"
                ,resourceBundle.getString("SimilarFileSingle") + " №2 (" + Formatter.getShortFilePath(this.comparer.getEndDirectoryName()) + ")"
                ,resourceBundle.getString("FileSize")
                ,","
                , "mb"
                ,"|");
        writer.printf("\r\n%-5s%-87.87s%9.9s%1s%3s%2s"
                ,"|"
                ,resourceBundle.getString("SimilarFileSingle") + " №3 (" + Formatter.getShortFilePath(this.comparer.getEndDirectoryName()) + ")"
                ,resourceBundle.getString("FileSize")
                ,","
                , "mb"
                ,"|");
        writer.print("\r\n ---------------------------------------------------------------------------------------------------------");
    }

    /*print title*/
    private void printTitle(PrintWriter writer,String title){
        writer.println();
        writer.println();
        writer.println("***********************************************************************************************************");
        writer.printf("%-5s%-100.100s%2s","*",title,"*");
        writer.printf("\r\n%-2s%-100.100s%5s","*","","*");
        writer.println("\r\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    /*print result of files were found*/
    private void printFound(PrintWriter writer, int quantity){
        ResourceBundle resourceBundle = this.comparer.getResourceBundle();
        if (quantity==0){
            writer.printf("%-5s%-100.100s%2s","*",resourceBundle.getString("NotFound"),"*");
        }else {
            writer.printf("%-5s%-8s%-1s%4d%-1s%-86.86s%2s", "*", resourceBundle.getString("Found"), " ",quantity, " ", resourceBundle.getString("Files"), "*");
        }
        writer.println("\r\n***********************************************************************************************************");
    }

    /*print list*/
    private void printFileList(PrintWriter writer, List<? extends Comparable> fileNameList){
        printFound(writer,fileNameList.size());
        for (Comparable fileName : fileNameList)
        {
            writer.println(fileName.toString());
        }
    }
}
