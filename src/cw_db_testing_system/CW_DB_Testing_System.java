package cw_db_testing_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.*;
import static cw_db_testing_system.StageConductor.initStageController;
import javafx.scene.image.Image;

/**
 *
 * @author Gleb
 */
public class CW_DB_Testing_System extends Application {

    @Override
    public void init() throws Exception {
        System.out.println("Stage is starting");            
        Service.getInstane();
    }

    @Override
    public void start(Stage stage) throws Exception {
        initStageController(stage);
        StageConductor.getInstance().BuildSceneOnPrimaryStage("RegistrationForm.fxml", "Authorisation");
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Stage is closing");
        Service.getInstane().destroyRegistry();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
