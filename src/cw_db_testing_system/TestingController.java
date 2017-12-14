/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import Model.Answer;
import Model.PassedTest;
import Model.Question;
import Model.Test;
import Model.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.Seance;
import utils.Service;

/**
 * FXML Controller class
 *
 * @author Gleb
 */
public class TestingController implements Initializable {

    @FXML
    private RadioButton rb1, rb2, rb3, rb4;

    @FXML
    private CheckBox cbOneCorrectAnswer, cb1, cb2, cb3, cb4;

    @FXML
    private Button btnPrevious, btnNext, btnFinishTest;

    @FXML
    private Label lbQuestionNumber, lbAnswer1, lbAnswer2, lbAnswer3, lbAnswer4, lbSubject, lbTitle;

    @FXML
    private TextArea taQuestionText;

    @FXML
    private SplitPane spitPane;

    public TestingController(Test t) {
        test = t;
    }

    private Test test;
    private PassedTest passedTest = new PassedTest();
    private Test tempTest;
    private Question currentQuestion;
    private int currentQuestionNumber;
    private ArrayList<Boolean> givenAnswerResults = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //0. Basic form-specific setup
        initHandlers();
        tempTest = new Test(test);

        //1. Set subject and test title labels
        lbSubject.setText(test.getSubject().getTitle());
        lbTitle.setText(test.getTitle());

        //2. Setting current question
        currentQuestionNumber = 0;
        currentQuestion = test.getQuestions().get(currentQuestionNumber);

        //3. Initing form
        initForm();
    }

    @FXML
    private void handleFinishTestButton(ActionEvent e) {
        saveCurrentQuestionAnswerResult();
        Session session = Service.getSessionFactory().openSession();
        passedTest.setId(randomUUID().toString());
        passedTest.setMark(countMark());
        passedTest.setTest(test);
        passedTest.setTestDate(new Date());
        User usr = (User) session.get(User.class,
                Seance.getInstance().getCurrentUser().getId());
        //Hibernate.initialize(passedTest.getTest().getUser());
        passedTest.setUser(usr);

        //5. Save passed test to DB        
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(passedTest);
            tx.commit();
        } catch (HibernateException x) {
            if (tx != null) {
                tx.rollback();
            }
        } catch (Exception ex) {
            System.out.println("super oops...");
            System.out.println(ex.getMessage());
        } finally {
            session.close();
        }

        StageConductor.getInstance().BuildSceneOnNewStage("testStats.fxml",
                "Test stat", new TestStatsController(passedTest));
        Stage st = (Stage) spitPane.getScene().getWindow();
        st.close();
    }

    @FXML
    private void handleNextButton(ActionEvent e) {
        saveCurrentQuestionAnswerResult();
        saveToTempTest();
        if (currentQuestionNumber == test.getQuestions().size() - 1) {
            btnNext.setDisable(true);
        } else {
            currentQuestionNumber++;
        }
        btnPrevious.setDisable(false);
        currentQuestion = test.getQuestions().get(currentQuestionNumber);
        initForm();
    }

    @FXML
    private void handlePreviousButton(ActionEvent e) {
        saveCurrentQuestionAnswerResult();
        saveToTempTest();
        currentQuestionNumber--;
        if (currentQuestionNumber == 0) {
            btnPrevious.setDisable(true);
        }
        btnNext.setDisable(false);
        currentQuestion = test.getQuestions().get(currentQuestionNumber);
        initForm();
    }

    private void saveToTempTest() {
        if (tempTest.getQuestions().size() <= currentQuestionNumber) {
            Question addedQuestion = new Question();
            tempTest.getQuestions().add(addedQuestion);
            addedQuestion.setTest(tempTest);
            addedQuestion.setId(randomUUID().toString());
            addedQuestion.setDeleted(false);
            for (int i = 0; i < tempTest.getQuestions().size(); i++) {
                Answer ans = new Answer();
                ans.setId(randomUUID().toString());
                ans.setQuestion(addedQuestion);
                addedQuestion.getAnswers().add(ans);
            }
        }

        ArrayList<Answer> newAnswers = new ArrayList<>();
        if (currentQuestion.getHasOnlyOneCorrectAnswer()) {
            newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, lbAnswer1.getText(), rb1.isSelected()));
            newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, lbAnswer2.getText(), rb2.isSelected()));
            if (currentQuestion.getAnswers().size() > 2) {
                newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, lbAnswer3.getText(), rb3.isSelected()));
            }
            if (currentQuestion.getAnswers().size() > 3) {
                newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, lbAnswer4.getText(), rb4.isSelected()));
            }
        } else {
            newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, lbAnswer1.getText(), cb1.isSelected()));
            newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, lbAnswer2.getText(), cb2.isSelected()));
            if (currentQuestion.getAnswers().size() > 2) {
                newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, lbAnswer3.getText(), cb3.isSelected()));
            }
            if (currentQuestion.getAnswers().size() > 3) {
                newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, lbAnswer4.getText(), cb4.isSelected()));
            }
        }
        tempTest.getQuestions().get(currentQuestionNumber).setAnswers(newAnswers);
    }

    private void loadFromTempTest() {
        if (tempTest.getQuestions().size() <= currentQuestionNumber) {
            return;
        }
        Question q = tempTest.getQuestions().get(currentQuestionNumber);
        if (q == null || q.getAnswers().size() == 0) {
            return;
        }
        if(givenAnswerResults.size() <= currentQuestionNumber){
            return;
        }
        if (q.getHasOnlyOneCorrectAnswer()) {
            rb1.setSelected(q.getAnswers().get(0).getIsCorrect());

            rb2.setSelected(q.getAnswers().get(1).getIsCorrect());

            if (q.getAnswers().size() > 2) {
                rb3.setSelected(q.getAnswers().get(2).getIsCorrect());
            }
            if (q.getAnswers().size() > 3) {
                rb4.setSelected(q.getAnswers().get(3).getIsCorrect());
            }
        } else {
            cb1.setSelected(q.getAnswers().get(0).getIsCorrect());

            cb2.setSelected(q.getAnswers().get(1).getIsCorrect());

            if (q.getAnswers().size() > 2) {
                cb3.setSelected(q.getAnswers().get(2).getIsCorrect());
            }
            if (q.getAnswers().size() > 3) {
                cb4.setSelected(q.getAnswers().get(3).getIsCorrect());
            }
        }
    }

    //in 0 to 100 range
    private double countMark() {
        double res = 0;
        for (Boolean givenAns : givenAnswerResults) {
            if (givenAns) {
                res++;
            }
        }
        res /= test.getQuestions().size();
        res *= 100;
        res *= 100;
        Math.round(res);
        res /= 100;
        return res;
    }

    private void initForm() {
        //0. Empty all question fields
        setAllQuestionFieldsEmpty();

        //1. Init question number label
        lbQuestionNumber.setText("Question"
                + (currentQuestionNumber + 1)
                + "/" + test.getQuestions().size());

        //2. Sec current question text
        taQuestionText.setText(currentQuestion.getText());

        //3. Init all the answers
        SetAnswersVisibility();
        loadFromTempTest();
        lbAnswer1.setText(currentQuestion.getAnswers().get(0).getText());
        lbAnswer2.setText(currentQuestion.getAnswers().get(1).getText());
        if (currentQuestion.getAnswers().size() > 2) {
            lbAnswer3.setText(currentQuestion.getAnswers().get(2).getText());
        }
        if (currentQuestion.getAnswers().size() > 3) {
            lbAnswer4.setText(currentQuestion.getAnswers().get(3).getText());
        }
    }

    private void initHandlers() {
        btnNext.setOnAction((event) -> {
            handleNextButton(event);
        });
        btnPrevious.setOnAction((event) -> {
            handlePreviousButton(event);
        });

        btnFinishTest.setOnAction((event) -> {
            handleFinishTestButton(event);
        });
    }

    private void saveCurrentQuestionAnswerResult() {
        List<Answer> currentAnswers = currentQuestion.getAnswers();
        Boolean res = false,
                cbres1 = true, cbres2 = true, cbres3 = true, cbres4 = true;
        if (currentQuestion.getHasOnlyOneCorrectAnswer()) {
            if ((currentAnswers.get(0).getIsCorrect())
                    && rb1.isSelected()) {
                res = true;
            }

            if ((currentAnswers.get(1).getIsCorrect())
                    && rb2.isSelected()) {
                res = true;
            }

            if (currentAnswers.size() > 2) {
                if ((currentAnswers.get(2).getIsCorrect())
                        && rb3.isSelected()) {
                    res = true;
                }
            }
            if (currentAnswers.size() > 3) {
                if ((currentAnswers.get(3).getIsCorrect())
                        && rb4.isSelected()) {
                    res = true;
                }
            }
        } else {
            if ((currentAnswers.get(0).getIsCorrect())
                    && !cb1.isSelected()) {
                cbres1 = false;
            }

            if ((currentAnswers.get(1).getIsCorrect())
                    && !cb2.isSelected()) {
                cbres1 = false;
            }

            if (currentAnswers.size() > 2) {
                if ((currentAnswers.get(2).getIsCorrect())
                        && !cb3.isSelected()) {
                    cbres1 = false;
                }
            }
            if (currentAnswers.size() > 3) {
                if ((currentAnswers.get(3).getIsCorrect())
                        && !cb4.isSelected()) {
                    cbres1 = false;
                }
            }
            if (cbres1 && cbres2 && cbres3 && cbres4) {
                res = true;
            }
        }
        if (currentQuestionNumber >= givenAnswerResults.size()) {
            givenAnswerResults.add(res);
        } else {
            givenAnswerResults.set(currentQuestionNumber, res);
        }
    }

    private void SetRadioButtonsVisible() {
        cb1.setVisible(false);
        rb1.setVisible(true);

        cb2.setVisible(false);
        rb2.setVisible(true);

        rb3.setVisible(false);
        rb4.setVisible(false);

        if (currentQuestion.getAnswers().size() > 2) {
            cb3.setVisible(false);
            rb3.setVisible(true);
        }
        if (currentQuestion.getAnswers().size() > 3) {
            cb4.setVisible(false);
            rb4.setVisible(true);
        }
    }

    private void SetCheckBoxesVisible() {
        rb1.setVisible(false);
        cb1.setVisible(true);

        rb2.setVisible(false);
        cb2.setVisible(true);

        cb3.setVisible(false);
        cb4.setVisible(false);

        if (currentQuestion.getAnswers().size() > 2) {
            rb3.setVisible(false);
            cb3.setVisible(true);
        }
        if (currentQuestion.getAnswers().size() > 3) {
            rb4.setVisible(false);
            cb4.setVisible(true);
        }
    }

    private void SetAnswersVisibility() {
        if (currentQuestion.getHasOnlyOneCorrectAnswer()) {
            SetRadioButtonsVisible();
        } else {
            SetCheckBoxesVisible();
        }
        lbAnswer1.setVisible(true);
        lbAnswer2.setVisible(true);
        lbAnswer3.setVisible(false);
        lbAnswer4.setVisible(false);

        if (currentQuestion.getAnswers().size() > 2) {
            lbAnswer3.setVisible(true);
        }
        if (currentQuestion.getAnswers().size() > 3) {
            lbAnswer4.setVisible(true);
        }
    }

    private void setAllQuestionFieldsEmpty() {
        taQuestionText.setText("");
        lbAnswer1.setText("");
        lbAnswer2.setText("");
        lbAnswer3.setText("");
        lbAnswer4.setText("");
        rb1.setSelected(false);
        rb2.setSelected(false);
        rb3.setSelected(false);
        rb4.setSelected(false);
        cb1.setSelected(false);
        cb2.setSelected(false);
        cb3.setSelected(false);
        cb4.setSelected(false);
        rb1.setVisible(false);
        rb2.setVisible(false);
        rb3.setVisible(false);
        rb4.setVisible(false);
        cb1.setVisible(false);
        cb2.setVisible(false);
        cb3.setVisible(false);
        cb4.setVisible(false);
    }
}
