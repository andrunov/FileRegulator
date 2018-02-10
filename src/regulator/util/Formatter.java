package regulator.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Class for custom formatters and other useful
 * methods for transfer primitive types
 */
public class Formatter {

    public static String doubleFormat(String pattern, double value ) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(value);
    }

    /*split 1-st par strong into words according length of word (2-nd parameter)
   * and sort that list*/
    public static List<String> splitString(String sentence, int wordLength){
        sentence = cutExtension(sentence);
        String[] arr = sentence.split("([0-9])|([\\s])|(\\.)|(,)|(\\()|(\\))|(-)|(_)|(\\?)|(\\!)|(:)|(;)|(&)");
        List<String> list = new ArrayList<>();
        for (String word : arr){
            if (word.length()>=wordLength){
                list.add(word.toLowerCase());
            }
        }
        Collections.sort(list);
        return removeDuplications(list);
    }

    /*cuts file extension*/
    private static String cutExtension(String fileName){
        int dotPosition = fileName.lastIndexOf('.');
        return fileName.substring(0,dotPosition);
    }

    /*removes duplications of elements in List<String>*/
    private static List<String> removeDuplications(List<String> list){
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            String first = iterator.next();
            while (iterator.hasNext()){
                String second = iterator.next();
                if (first.equals(second)){
                    iterator.remove();
                }else {
                    break;
                }
            }
        }
        return list;
    }

    /*transform String[] to String*/
    public static String getArrayAsString(String[] strings){
        StringBuilder sb = new StringBuilder();
        for (String s : strings){
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();
    }

    /*return true if string parameter is null or empty or spase*/
    public static boolean stringIsEmpty(String string){
        if (string == null) return true;
        if (string.isEmpty()) return true;
        String stringTrimmed = string.replaceAll("([\\s])","");
        return stringTrimmed.equals("");
    }

    /*split string in rows according roeLength in parameter*/
    public static List<String> splitStringInRows(String string, int rowLength){
        List<String> result = new ArrayList<>();
        int counter = string.length()/rowLength;
        for (int i = 0; i <counter; i++){
            result.add(string.substring(i*rowLength,(i+1)*rowLength));
        }
        result.add(string.substring(counter*rowLength));
        return result;
    }

    /*transform full filePath to filename only*/
    public static String getShortFilePath(String filePath){
        return filePath.substring(0,3) + "..." + filePath.substring(filePath.lastIndexOf('\\'));
    }

    /*adjust control's text size according app's height*/
    public static double getTextSize(Double height){
        double defaultSize = 8.0; //default size
        double sizeByHeight = 0.06 * height;
        return sizeByHeight > defaultSize ? sizeByHeight : defaultSize ;
    }

}
