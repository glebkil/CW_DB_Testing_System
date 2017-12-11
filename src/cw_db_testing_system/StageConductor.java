/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import utils.Seance;
import utils.Service;

/**
 *
 * @author Gleb
 */
public class StageConductor{

    private Stage stage;
    private Scene authentificatinScene = null;
    private Scene studentFormScene = null;
    private Scene teacherFormScene = null;
    private Scene adminFormScene = null;
    private static StageConductor instance;

    private StageConductor(Stage stage) {
        this.stage = stage;
    }

    public static void initStageController(Stage st) {
        if (instance == null) {
            instance = new StageConductor(st);
        }
    }

    public static StageConductor getInstance() {
        return instance;
    }

    public void setScene() throws Exception {
        Service service = Service.getInstane();
        Session session = Service.getSessionFactory().openSession();
        Seance seance = Seance.getInstance();
        if (seance.getCurrentUser() == null) {
            if (authentificatinScene == null) {
                authentificatinScene = new Scene(FXMLLoader
                        .load(getClass()
                                .getResource("RegistrationForm.fxml")));
            }
            stage.setScene(authentificatinScene);
        } else if (seance.getCurrentUser().getRole().getName().equals("student")) {
            if (studentFormScene == null) {
                studentFormScene = new Scene(FXMLLoader
                        .load(getClass()
                                .getResource("studentForm.fxml")));
            }
            stage.setScene(studentFormScene);
        } else if (seance.getCurrentUser().getRole().getName().equals("teacher")) {
            if (teacherFormScene == null) {
                teacherFormScene = new Scene(FXMLLoader
                        .load(getClass()
                                .getResource("teacherForm.fxml")));
            }
            stage.setScene(teacherFormScene);
        } else if (seance.getCurrentUser().getRole().getName().equals("admin")) {
            if (adminFormScene == null) {
                adminFormScene = new Scene(FXMLLoader
                        .load(getClass()
                                .getResource("adminForm.fxml")));
            }
            stage.setScene(adminFormScene);
        }
        stage.show();
        session.close();
    }

    public FXMLLoader BuildStage(String fxmlResource, String title) {
        try {
            Stage st = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlResource));
            Scene scene = new Scene(loader.load());
            st.setScene(scene);
            st.setTitle(title);
            st.show();   
            return loader;
        } catch (Exception e) {
            System.out.println(e.getMessage() );
            e.printStackTrace();
            return null;
        } 
    }
}
