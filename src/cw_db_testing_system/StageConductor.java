/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hibernate.Session;
import utils.Seance;
import utils.Service;

/**
 *
 * @author Gleb
 */
public class StageConductor {

    private Stage primaryStage;
//    private Scene authentificatinScene = null;
//    private Scene studentFormScene = null;
//    private Scene teacherFormScene = null;
//    private Scene adminFormScene = null;
    private static StageConductor instance;

    private StageConductor(Stage stage) {
        this.primaryStage = stage;
    }

    public static void initStageController(Stage st) {
        if (instance == null) {
            instance = new StageConductor(st);
        }
    }

    public static StageConductor getInstance() {
        return instance;
    }

    private void setIcon(Stage st) {
        st.getIcons().add(new Image("file:C:\\Projects\\NetBeans\\CW_DB_Testing_System\\src\\resources\\icon.png"));
    }

    public void BuildSceneOnPrimaryStage(String fxmlResource, String title) {
        try {            
            setIcon(primaryStage);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlResource));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void BuildSceneOnNewStage(String fxmlResource, String title) {
        try {
            Stage st = new Stage();
            setIcon(st);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlResource));
            Scene scene = new Scene(loader.load());
            st.setScene(scene);
            st.setTitle(title);
            st.setResizable(false);
            st.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void BuildSceneOnNewStage(String fxmlResource, String title, Object controller) {
        try {
            Stage st = new Stage();
            setIcon(st);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlResource));
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            st.setScene(scene);
            st.setResizable(false);
            st.setTitle(title);
            st.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
