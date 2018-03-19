package regulator;
/**
 * Class with main method
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import regulator.controller.MainController;
import regulator.controller.SettingsController;
import regulator.util.AppPreferences;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/*Main app JavaFX class */
public class MainApp extends Application {

    /*primary app stage*/
    private Stage primaryStage;

    /*root layout element*/
    private AnchorPane rootLayout;

    /*link to main controller*/
    private MainController mainController;

    /*main method*/
    public static void main(String[] args) {
        launch(args);
    }

    /*entry JavaFX method*/
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Directory compares");
        this.primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream( "/regulator/resources/images/appImage.png" )));
        initRootLayout(new Locale("ru","RU"));

        this.primaryStage.heightProperty().addListener(mainController.stageSizeListener);
        this.primaryStage.setWidth(AppPreferences.getMainWindowWidth());
        this.primaryStage.setHeight(AppPreferences.getMainWindowHeight());
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        AppPreferences.setMainWindowHeight(this.getPrimaryStage().getHeight());
        AppPreferences.setMainWindowWidth(this.getPrimaryStage().getWidth());
    }

    /**
     * open main window
     */
    public void initRootLayout(Locale locale) {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("regulator.resources.bundles.Locale", locale));
            loader.setLocation(MainApp.class.getResource("view/MainView.fxml"));
            rootLayout = (AnchorPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Give the mainController access to the main app.
            mainController = loader.getController();
            mainController.setMainApp(this);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*open settings window*/
    public void showSettingsEditDialog(MainController parentController) {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(parentController.getResourceBundle());
            loader.setLocation(MainApp.class.getResource("view/SettingsView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create dialog window Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Settings");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // create and adjust controller
            SettingsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setFilter(parentController.getFilter());
            controller.setFieldsValues();
            controller.setResourceBundle(parentController.getResourceBundle());
            controller.setMainController(parentController);

            dialogStage.heightProperty().addListener(controller.stageSizeListener);
            dialogStage.setWidth(AppPreferences.getSettingsWindowWidth());
            dialogStage.setHeight(AppPreferences.getSettingsWindowHeight());


            // open dialog stage and wait till user close it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*getter for primary stage*/
    public Stage getPrimaryStage() {
        return primaryStage;
    }


}
