package regulator.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

/**
 * Class for output strings info
 */
public class Writer {

    /*report path*/
    private String reportName;

    /*encoding*/
    private String encoding;

    /*constructor*/
    public Writer(String encoding, String reportName) {
        this.encoding = encoding;
        this.reportName = reportName;
    }

    /*write text to file*/
    public boolean write(String text, ResourceBundle resourceBundle){
        boolean result = false;
        try{
            PrintWriter writer = new PrintWriter(reportName, "UTF-8");
            writer.print(text);
            writer.close();
            result = true;
        } catch (IOException e) {
            Message.errorAlert(resourceBundle,e);
        }
        return result;
    }

}
