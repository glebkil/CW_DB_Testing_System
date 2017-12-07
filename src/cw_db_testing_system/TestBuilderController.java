/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import Model.Subject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import utils.Service;

/**
 * FXML Controller class
 *
 * @author Gleb
 */
public class TestBuilderController implements Initializable {

    private int currentQuestion = 1;
    private int numberOfAnswers = 2;
    private boolean onlyOneCorrectAnswer = true;
    /**
     * Initializes the controller class.
     */

    @FXML
    private ChoiceBox cbSubjects;

    @FXML
    public void handleAddSubject() {
        StageConductor.getInstance().BuildStage("AddingSubject.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Subject> subjects = Service.getInstane().getSubjects();
        ArrayList<String> subjectNames = new ArrayList();
        if (subjects != null) {        
            for (Subject subject : (List<Subject>) subjects) {
                subjectNames.add(subject.getTitle());
            }
            ObservableList<String> ol = FXCollections.observableArrayList(subjectNames);
            cbSubjects.setItems(ol);
            cbSubjects.getSelectionModel().selectFirst();
        }
    }

}
