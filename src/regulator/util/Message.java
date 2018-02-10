package regulator.util;

import javafx.scene.control.Alert;

import java.util.ResourceBundle;

/**
 * Class for show messages
 */
public class Message {

    /*show info message*/
    public static void info(ResourceBundle resourceBundle, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("InfoTitle"));
        alert.setHeaderText(resourceBundle.getString("InfoHeaderTex"));
        alert.setContentText(resourceBundle.getString(message));
        alert.showAndWait();
    }

    /*show alert message*/
    public static void warningAlert(ResourceBundle resourceBundle, String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(resourceBundle.getString("AlertTitle"));
        alert.setHeaderText(resourceBundle.getString("WarningAlertHeaderTex"));
        alert.setContentText(resourceBundle.getString(message));
        alert.showAndWait();
    }

    /*show error message*/
    public static void errorAlert(ResourceBundle resourceBundle, Throwable throwable){
        if (throwable.getMessage()!=null) {
            errorAlert(resourceBundle,throwable.getMessage());
        }else {
            errorAlert(resourceBundle,throwable.toString());
        }
    }

    /*show error message*/
    public static void errorAlert(ResourceBundle resourceBundle, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("ErrorTitle"));
        alert.setHeaderText(resourceBundle.getString("ErrorAlertHeaderTex"));
        alert.setContentText(message);
        alert.showAndWait();
    }

}
