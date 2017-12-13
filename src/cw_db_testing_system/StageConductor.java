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
    private Scene authentificatinScene = null;
    private Scene studentFormScene = null;
    private Scene teacherFormScene = null;
    private Scene adminFormScene = null;
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

    public void setPrimaryScene() throws Exception {
        //Service service = Service.getInstane();
        //Session session = Service.getSessionFactory().openSession();
        setIcon(primaryStage);
        Seance seance = Seance.getInstance();
        if (seance.getCurrentUser() == null) {
            if (authentificatinScene == null) {
                authentificatinScene = new Scene(FXMLLoader
                        .load(getClass()
                                .getResource("RegistrationForm.fxml")));
            }
            primaryStage.setScene(authentificatinScene);
            primaryStage.setTitle("Authentification");
        } else if (seance.getCurrentUser().getRole().getName().equals("student")) {
            if (studentFormScene == null) {
                studentFormScene = new Scene(FXMLLoader
                        .load(getClass()
                                .getResource("studentForm.fxml")));
            }
            primaryStage.setScene(studentFormScene);
            primaryStage.setTitle("Student panel");
        } else if (seance.getCurrentUser().getRole().getName().equals("teacher")) {
            if (teacherFormScene == null) {
                teacherFormScene = new Scene(FXMLLoader
                        .load(getClass()
                                .getResource("teacherForm.fxml")));
            }
            primaryStage.setScene(teacherFormScene);
            primaryStage.setTitle("Teacher panel");
        } else if (seance.getCurrentUser().getRole().getName().equals("admin")) {
            if (adminFormScene == null) {
                adminFormScene = new Scene(FXMLLoader
                        .load(getClass()
                                .getResource("adminForm.fxml")));
            }
            primaryStage.setScene(adminFormScene);
            primaryStage.setTitle("Admin panel");
        }
        primaryStage.show();
        //session.close();
    }

    public FXMLLoader BuildStage(String fxmlResource, String title) {
        FXMLLoader loader = null;
        try {
            Stage st = new Stage();
            setIcon(st);
            loader = new FXMLLoader(getClass().getResource(fxmlResource));
            Scene scene = new Scene(loader.load());
            st.setScene(scene);
            st.setTitle(title);
            st.show();
            return loader;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return loader;
        }
    }

    public void BuildStageWithController(String fxmlResource, String title, Object controller) {
        FXMLLoader loader = null;
        try {
            Stage st = new Stage();
            setIcon(st);
            loader = new FXMLLoader(getClass().getResource(fxmlResource));
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            st.setScene(scene);
            st.setTitle(title);
            st.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
