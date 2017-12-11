/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import Model.Question;
import Model.Subject;
import Model.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import utils.Service;

/**
 * FXML Controller class
 *
 * @author Gleb
 */
public class TestBuilderController implements Initializable {

    private Test test;
    private int currentQuestion;

    @FXML
    private Button btnPrevious, btnNext;

    @FXML
    private ChoiceBox cbSubjects;

    @FXML
    private TextField tfAnswer1, tfAnswer2, tfAnswer3, tfAnswer4;

    @FXML
    private ChoiceBox<Integer> cbNumOfAnswers;

    @FXML
    private RadioButton rb1, rb2, rb3, rb4;

    @FXML
    private CheckBox cbOneCorrectAnswer, cb1, cb2, cb3, cb4;

    @FXML
    private Label lbQuestionNumber;

    @FXML
    private TextArea taQuestionText;

    @FXML
    public void handleAddSubject() {
        StageConductor.getInstance().BuildStage("AddingSubject.fxml", "Adding subject");
    }

    @FXML
    private void handleOneCorrectAnswerCheckBox(ActionEvent event) {
        SetAnswersVisibility();
    }

    @FXML
    private void handleNumOfAnswersChoiceBox() {
        SetAnswersVisibility();
    }

    @FXML
    private void handlePreviousButton() {
        currentQuestion--;
        if (currentQuestion == 0) {
            btnPrevious.setVisible(false);
        }
    }

    @FXML
    private void handleNextButton() {
        currentQuestion++;
        btnPrevious.setVisible(true);

    }

    public void setTest(Test t) {
        test = t;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbSubjects.setTooltip(new Tooltip("Select the subject of your test"));
        
        //1. Add subjects 
        populateSubjectsChoiceBox();

        //2. Populating choicebox of number of answers            
        cbNumOfAnswers.getItems().addAll(2, 3, 4);
        cbNumOfAnswers.getSelectionModel().selectLast();
        cbNumOfAnswers.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldV, newV) -> SetAnswersVisibility());
        
        //3. Setting current question
        currentQuestion = 0;
        
        //4. Initig current question
        initCurrentQuestion();
    }

    private void initCurrentQuestion() {
        setAllQuestionFieldsEmpty
        //0. Do nothing if there is no question yet
        if(test.getQuestions().size() <= currentQuestion){
            return;
        }
        //1. Init question number label
        lbQuestionNumber.setText("Question" + (currentQuestion + 1));
        
        //2. Setting proper number of options and their type
        cbOneCorrectAnswer.setSelected(test.getQuestions().get(currentQuestion).getOneCorrectAnswer());
        cbNumOfAnswers.getSelectionModel().select(test.getQuestions().get(currentQuestion).getAnswers().size());
        
        //3. Populating question text and options
        
        //4. Showing rigth answers
        
    }
    
    private void setAllQuestionFieldsEmpty(){
        taQuestionText.setText("");
        tfAnswer1.setText("");
        tfAnswer2.setText("");
        tfAnswer3.setText("");
        tfAnswer4.setText("");
    }

    private void populateSubjectsChoiceBox() {
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

    private void SetRadioButtonsVisible() {

        cb1.setVisible(false);
        cb2.setVisible(false);
        cb3.setVisible(false);
        cb4.setVisible(false);

        rb1.setVisible(true);
        rb2.setVisible(true);
        rb3.setVisible(true);
        rb4.setVisible(true);

        if (cbNumOfAnswers.getValue().intValue() == 3) {
            rb4.setVisible(false);
        }
        if (cbNumOfAnswers.getValue().intValue() == 2) {
            rb3.setVisible(false);
            rb4.setVisible(false);
        }
    }

    private void SetCheckBoxesVisible() {
        rb1.setVisible(false);
        rb2.setVisible(false);
        rb3.setVisible(false);
        rb4.setVisible(false);

        cb1.setVisible(true);
        cb2.setVisible(true);
        cb3.setVisible(true);
        cb4.setVisible(true);

        if (cbNumOfAnswers.getValue().intValue() == 3) {
            cb4.setVisible(false);
        }
        if (cbNumOfAnswers.getValue().intValue() == 2) {
            cb3.setVisible(false);
            cb4.setVisible(false);
        }
    }

    private void SetAnswersVisibility() {
        if (cbOneCorrectAnswer.isSelected()) {
            SetRadioButtonsVisible();
        } else {
            SetCheckBoxesVisible();
        }
        tfAnswer1.setVisible(true);
        tfAnswer2.setVisible(true);
        tfAnswer3.setVisible(true);
        tfAnswer4.setVisible(true);

        if (cbNumOfAnswers.getValue().intValue() == 3) {
            tfAnswer4.setVisible(false);
        }
        if (cbNumOfAnswers.getValue().intValue() == 2) {
            tfAnswer3.setVisible(false);
            tfAnswer4.setVisible(false);
        }
    }
}
