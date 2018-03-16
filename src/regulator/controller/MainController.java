package regulator.controller;


import regulator.MainApp;
import regulator.model.FileRenamer;
import regulator.util.AppPreferences;
import regulator.util.FileFilter;
import regulator.util.Formatter;
import regulator.util.Message;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/*controller for MainView.fxml window*/
public class MainController implements Initializable {

    /*label of first directory*/
    @FXML
    private Label firstDirLbl;

    /*info label*/
    @FXML
    private Label infoLbl;

    /*result label*/
    @FXML
    private Label resultLbl;

    /*button for firs directory selection*/
    @FXML
    private Button firstDirSelectBtn;

    /*button for change language pocket*/
    @FXML
    private Button changeLocalButton;

    /*button for start comparing procedure*/
    @FXML
    private Button executeButton;

    /*button for exit application*/
    @FXML
    private Button openResultBtn;

    /*button for clear resources to default*/
    @FXML
    private Button clearBtn;

    /*button for open settings window*/
    @FXML
    private Button settingsBtn;

    /*button for open application info window*/
    @FXML
    private Button aboutBtn;

    /*button for exit application*/
    @FXML
    private Button exitBtn;

    /*language pocket*/
    private ResourceBundle resourceBundle;

    /*first choose directory for comparing*/
    private File directory;

    /*reference to rename engine class*/
    private FileRenamer renamer;

    /*reference to rename engine class*/
    private FileFilter filter;

    /* Reference to the main application*/
    private MainApp mainApp;

    /*desktop uses for open files just from JavaFX application*/
    private Desktop desktop;

    /*constructor*/
    public MainController() {
        if (Desktop.isDesktopSupported()) {
            this.desktop = Desktop.getDesktop();
        }
    }

    /**
     * Is called by the main application to give a reference back to itself.
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /*choose first directory*/
    @FXML
    private void choseDirectory(){
        /*not null reportName means that
        some compares happens before.
        reset renamer in such case*/
//        if (this.renamer.getReportName()!= null){
//            clear();
//        }
        File directory = chooseDirectory();
        if (directory != null) {
            this.directory = directory;
            AppPreferences.setDirectory(directory.getParentFile());
            setTextDirLabel(this.firstDirLbl, "FirstDirectory", getDirInfo(directory));
            updateTextInfoLbl();
        }
    }


    /*start comparing procedure*/
    @FXML
    private void executeComparing(){
        if (this.directory != null) {
            this.renamer = new FileRenamer(this.directory.getAbsolutePath());
        }

        this.renamer.setResourceBundle(this.resourceBundle);
        try{
//            if(this.renamer.compare()) {
//                setTextDirLabel(this.resultLbl, "Result", getFileInfo(this.renamer.getReportName()));
//                setVisibility(true);
//            }
        }
        catch (Exception e){
            Message.errorAlert(this.resourceBundle,e);
        }
    }

    /*open dialog to choose directory*/
    private File chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File initialDirectory = AppPreferences.getDirectory();
        if ((initialDirectory != null)&&(initialDirectory.exists())) {
            directoryChooser.setInitialDirectory(AppPreferences.getDirectory());
        }
        return directoryChooser.showDialog(null);
    }

    /*initialize language pocket and set visibility to window elements*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        setVisibility(false);
    }

    /*change pocket language*/
    @FXML
    private void changeLocale(){
        if (this.resourceBundle.getLocale().getLanguage().equalsIgnoreCase("ru")){
            this.resourceBundle = ResourceBundle.getBundle("regulator.resources.bundles.Locale",new Locale("en"));
        }else {
            this.resourceBundle = ResourceBundle.getBundle("regulator.resources.bundles.Locale",new Locale("ru"));
        }
        updateLocalText();
    }

    /*update text of window elements*/
    private void updateLocalText(){
        updateTextInfoLbl();
        this.firstDirSelectBtn.setText(this.resourceBundle.getString("Select"));
        this.changeLocalButton.setText(this.resourceBundle.getString("ChangeLocal"));
        this.executeButton.setText(this.resourceBundle.getString("Compare"));
        this.clearBtn.setText(this.resourceBundle.getString("Clear"));
        this.openResultBtn.setText(this.resourceBundle.getString("Open"));
        this.settingsBtn.setText(this.resourceBundle.getString("Settings"));
        this.aboutBtn.setText(this.resourceBundle.getString("AppInfo"));
        this.exitBtn.setText(this.resourceBundle.getString("Exit"));
    }

    /*updates text for infoLbl Label depending of
    * directory and secondDirectory directories*/
    private void updateTextInfoLbl(){
        setTextDirLabel(firstDirLbl,"FirstDirectory",getDirInfo(directory));
//        String reportName = this.renamer.getReportName();
//        if(reportName != null) {
//            setTextDirLabel(resultLbl, "Result", getFileInfo(this.renamer.getReportName()));
//        }
        if (directory ==null){
            infoLbl.setText(resourceBundle.getString("InfoDefault"));
        }
        else {
            infoLbl.setText(resourceBundle.getString("NormaliseFiles"));
        }
    }

    /*updates text for several Labels*/
    private void setTextDirLabel(Label label, String bundleKey, String infoPath){
         label.setText(resourceBundle.getString(bundleKey) + infoPath);
    }

    /**/
    private String getDirInfo(File directory){
        String result = "";
        if (directory != null) {
            result = ": " + directory.getPath();
        }
        return result;
    }

    /*return string-represent directory name with closest parent directory*/
    private String getFileInfo(String filePath){
        String result = "";
        File file = new File(filePath);
        if (file.exists()){
            result = ": " + file.getParentFile().getPath() + "\\" + file.getName();
        }
        return result;
    }

    /*open saved txt-result file*/
    @FXML
    private void openResult(){
        try {
            assert this.desktop != null;
//            this.desktop.open(new File(this.renamer.getReportName()));
        } catch (Exception e) {
            Message.errorAlert(this.resourceBundle,e);
        }
    }

    /*set visibility to open result button and label*/
    private void setVisibility(boolean visibility){
//        this.resultLbl.setVisible(visibility);
//        this.openResultBtn.setVisible(visibility);
    }

    /*clear fields to default*/
    @FXML
    private void clear(){
//        this.renamer.cleanFields();
        this.directory = null;
        updateTextInfoLbl();
        setVisibility(false);
    }

    /*open settings window*/
    @FXML
    private void openSettings(){
        mainApp.showSettingsEditDialog(this.resourceBundle, this.filter);
    }

    /*show application info*/
    @FXML
    private void showAppInfo(){
        Message.info(this.resourceBundle,"AboutApp");
    }

    /*exit application*/
    @FXML
    private void doExitApp(){
        this.mainApp.getPrimaryStage().close();
    }

    /*listener for observe change height of main window */
    public ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
    {
        double height = this.mainApp.getPrimaryStage().getHeight();
        this.firstDirLbl.setStyle("-fx-font-size:"+ Formatter.getTextSize(height)+";");
        this.infoLbl.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
        this.resultLbl.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
        this.firstDirSelectBtn.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
        this.changeLocalButton.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
        this.executeButton.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
        this.openResultBtn.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
        this.clearBtn.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
        this.settingsBtn.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
        this.aboutBtn.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
        this.exitBtn.setStyle("-fx-font-size:"+Formatter.getTextSize(height)+";");
    };

}
