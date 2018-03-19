package regulator.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import regulator.util.AppPreferences;
import regulator.util.FileFilter;
import regulator.util.Formatter;
import regulator.util.Message;

import java.util.ResourceBundle;

//*Controller class for SettingsWiew.fxml window*/
public class SettingsController {

    /*window stage*/
    private Stage dialogStage;

    /*link to parent program controller*/
    private MainController mainController;

    /*file filter*/
    private FileFilter filter;

    /*language pocket*/
    private ResourceBundle resourceBundle;

    /*field for filter text*/
    @FXML
    private TextField filterTextField;

    /*button for save settings and exit*/
    @FXML
    private Button saveBtn;

    /*button for cancel changes and exit*/
    @FXML
    private Button cancelBtn;

    /*button for info for filter field*/
    @FXML
    private Button questionFilter;

    /*label for for filter field*/
    @FXML
    private Label filterLbl;

    /*set language pocket*/
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /*set filter*/
    public void setFilter(FileFilter filter) {
        this.filter = filter;
    }

    /*set values of class fields*/
    public void setFieldsValues(){
        if (this.filter != null) {
            this.filterTextField.setText(Formatter.getArrayAsString(this.filter.getExtensions()));
        }
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * set dialog stage for this window
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    /**
     * Cancel button click handle
     */
    @FXML
    private void cancel() {
        AppPreferences.setSettingsWindowHeight(this.dialogStage.getHeight());
        AppPreferences.setSettingsWindowWidth(this.dialogStage.getWidth());
        dialogStage.close();
    }

    /**
     * Save button click handle
     */
    @FXML
    private void save() {
        if (isInputValid()) {
            String[] extensions = new String[0];
            if (!Formatter.stringIsEmpty(this.filterTextField.getText())){
                extensions = this.filterTextField.getText().split(" ");
            }
            this.filter = new FileFilter(extensions);
            mainController.setFilter(this.filter);
            AppPreferences.setFilterExtensions(extensions);
            AppPreferences.setSettingsWindowHeight(this.dialogStage.getHeight());
            AppPreferences.setSettingsWindowWidth(this.dialogStage.getWidth());
            dialogStage.close();
        }
    }

    /*show info about filter*/
    @FXML
    private void showFilterInfo(){
        Message.info(this.resourceBundle,"FilterInfo");
    }

    /*show info about min length of word*/
    @FXML
    private void showMinLengthInfo(){
        Message.info(this.resourceBundle,"MinLengthInfo");
    }

    /*show info about absolute and relative path*/
    @FXML
    private void showPathInfo(){
        Message.info(this.resourceBundle,"PathInfo");
    }

    /*check that user input correct data*/
    private boolean isInputValid() {
        String filterExtensions = this.filterTextField.getText();
        if ((!filterExtensions.matches("[a-zA-Z0-9\\s]+"))&&(!filterExtensions.isEmpty())){
            Message.errorAlert(this.resourceBundle,"FilterExtensionException");
            return false;
        }
        return true;
    }

    /*listener for observe change height of settings window */
    public ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
    {
        double height = this.dialogStage.getHeight()*2;
        this.filterTextField.setStyle("-fx-font-size:"+ Formatter.getTextSize(height)+";");
        this.saveBtn.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
        this.cancelBtn.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
        this.questionFilter.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
        this.filterLbl.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
    };

}
