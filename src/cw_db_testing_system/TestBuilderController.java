/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import Model.Answer;
import Model.Question;
import Model.Subject;
import Model.Test;
import Model.User;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import static java.util.UUID.randomUUID;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import utils.CustomException;
import utils.Seance;
import utils.Service;

/**
 * FXML Controller class
 *
 * @author Gleb
 */
public class TestBuilderController implements Initializable {

    @FXML
    private Button btnPrevious, btnNext, btnAddSubject, btnSaveTest, btnRefresh;

    @FXML
    private TextField tfAnswer1, tfAnswer2, tfAnswer3, tfAnswer4, tfTestTitle;

    @FXML
    private ChoiceBox<Integer> cbNumOfAnswers;

    @FXML
    private ChoiceBox<String> cbSubjects;

    @FXML
    private RadioButton rb1, rb2, rb3, rb4;

    @FXML
    private CheckBox cbOneCorrectAnswer, cb1, cb2, cb3, cb4;

    @FXML
    private Label lbQuestionNumber, lbErrorMessage;

    @FXML
    private TextArea taQuestionText;

    @FXML
    private SplitPane spitPane;

    public TestBuilderController(Test t) {
        test = t;
    }

    private Test test;
    private Question currentQuestion;
    private int currentQuestionNumber;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //0. Basic form-specific setup
        initHandlers();
        cbSubjects.setTooltip(new Tooltip("Select the subject of your test"));

        //1. Add subjects 
        populateSubjectsChoiceBox();

        //2. Setting current question
        currentQuestionNumber = 0;

        //3. Populating choicebox of number of answers            
        cbNumOfAnswers.getItems().addAll(2, 3, 4);
        cbNumOfAnswers.getSelectionModel().selectLast();

        //4. Initing current question
        initCurrentQuestion();

        //5. Initing form
        initForm();
    }

    @FXML
    private void handleRefreshButton(ActionEvent e) {
        List<Subject> subjects = Service.getInstane().getSubjects();
        ArrayList<String> subjectNames = new ArrayList<String>();
        if (subjects != null) {
            for (Subject subject : (List<Subject>) subjects) {
                subjectNames.add(subject.getTitle());
            }
            ObservableList<String> ol = FXCollections.observableArrayList(subjectNames);
            cbSubjects.setItems(ol);
            cbSubjects.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void handleSaveTestButton(ActionEvent e) {
        //0. Check if everything has values
        saveCurentQuestionData();
        lbErrorMessage.setText("");
        if (tfTestTitle.getText() == null) {
            lbErrorMessage.setTextFill(Color.web("#911111"));
            lbErrorMessage.setText("Looks like you have not added a test title");
            return;
        }
        for (Question q : test.getQuestions()) {
            if (q.getText() == null || q.getHasNoCorrectAnswers()) {
                lbErrorMessage.setTextFill(Color.web("#911111"));
                lbErrorMessage.setText("Looks like you have no filled text of one of the questions");
                return;
            }
            for (Answer a : q.getAnswers()) {
                if (q.getText() == null) {
                    lbErrorMessage.setTextFill(Color.web("#911111"));
                    lbErrorMessage.setText("Looks like you have not filled all the answers in one of the questions");
                    return;
                }
            }

        }

        Session session = Service.getSessionFactory().openSession();

        //1. Add test subject
        Criteria cr = session.createCriteria(Subject.class);
        cr.add(Restrictions.eq("title", cbSubjects.getSelectionModel().getSelectedItem()));
        Subject subject = (Subject) cr.list().get(0);
        System.out.println(subject.getTitle());
        test.setSubject(subject);

        //2. Add test user      
        test.setUser((User) session.get(User.class, Seance.getInstance().getCurrentUser().getId()));

        //3. Add title
        test.setTitle(tfTestTitle.getText());

        //4. It's not deleted
        test.setDeleted(false);

        //5. Save test to DB        
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (test.getId() == null) {
                test.setId(randomUUID().toString());
                session.save(test);
            } else {
                session.update(test);
            }

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

        Stage st = (Stage) spitPane.getScene().getWindow();
        st.close();
    }

    @FXML
    private void handleAddSubject(ActionEvent e) {
        StageConductor.getInstance().BuildSceneOnNewStage("AddingSubject.fxml", "Adding subject");
    }

    @FXML
    private void handleOneCorrectAnswerCheckBox(ActionEvent e) {
        SetAnswersVisibility();
    }

    @FXML
    private void handlePreviousButton(ActionEvent e) {
        saveCurentQuestionData();
        currentQuestionNumber--;
        initCurrentQuestion();
        initForm();
        if (currentQuestionNumber == 0) {
            btnPrevious.setDisable(true);
        }

    }

    @FXML
    private void handleNextButton(ActionEvent e) {
        saveCurentQuestionData();
        currentQuestionNumber++;
        initCurrentQuestion();
        initForm();
        btnPrevious.setDisable(false);

    }

    private void eraseErrorMessage(KeyEvent e) {
        lbErrorMessage.setText("");
    }

    private void initForm() {
        //0. Empty all question fields
        setAllQuestionFieldsEmpty();

        //1. Init question number label
        lbQuestionNumber.setText("Question" + (currentQuestionNumber + 1));

        //2. Init proper number of options and their type
        cbNumOfAnswers.getSelectionModel().select((Integer) currentQuestion.getAnswers().size());

        cbOneCorrectAnswer.setSelected(currentQuestion.getHasOnlyOneCorrectAnswer()
                || currentQuestion.getHasNoCorrectAnswers());
        SetAnswersVisibility();

        if (currentQuestion.getAnswers().size() != 0) {
            taQuestionText.setText(currentQuestion.getText());

            tfAnswer1.setText(currentQuestion.getAnswers().get(0).getText());
            tfAnswer2.setText(currentQuestion.getAnswers().get(1).getText());
            if (currentQuestion.getAnswers().size() > 2) {
                tfAnswer3.setText(currentQuestion.getAnswers().get(2).getText());
            }
            if (currentQuestion.getAnswers().size() > 3) {
                tfAnswer4.setText(currentQuestion.getAnswers().get(3).getText());
            }

            //4. Init correct answer(s)
            if (currentQuestion.getHasOnlyOneCorrectAnswer()) {
                rb1.setSelected(currentQuestion.getAnswers().get(0).getIsCorrect());

                rb2.setSelected(currentQuestion.getAnswers().get(1).getIsCorrect());

                if (currentQuestion.getAnswers().size() > 2) {
                    rb3.setSelected(currentQuestion.getAnswers().get(2).getIsCorrect());
                }
                if (currentQuestion.getAnswers().size() > 3) {
                    rb4.setSelected(currentQuestion.getAnswers().get(3).getIsCorrect());
                }
            } else {
                cb1.setSelected(currentQuestion.getAnswers().get(0).getIsCorrect());

                cb2.setSelected(currentQuestion.getAnswers().get(1).getIsCorrect());

                if (currentQuestion.getAnswers().size() > 2) {
                    cb3.setSelected(currentQuestion.getAnswers().get(2).getIsCorrect());
                }
                if (currentQuestion.getAnswers().size() > 3) {
                    cb4.setSelected(currentQuestion.getAnswers().get(3).getIsCorrect());
                }
            }
        }
        //3. Init question text and all the options

        //5. Init test title
        tfTestTitle.setText(test.getTitle());

        //6. Init subject box
        if (test.getSubject() != null) {
            cbSubjects.getSelectionModel().select(test.getSubject().getTitle());
        }
    }

    private void initHandlers() {
        cbOneCorrectAnswer.setOnAction((event) -> {
            handleOneCorrectAnswerCheckBox(event);
        });
        btnAddSubject.setOnAction((event) -> {
            handleAddSubject(event);
        });
        btnNext.setOnAction((event) -> {
            handleNextButton(event);
        });
        btnPrevious.setOnAction((event) -> {
            handlePreviousButton(event);
        });

        btnSaveTest.setOnAction((event) -> {
            handleSaveTestButton(event);
        });
        tfTestTitle.setOnKeyPressed((event) -> {
            eraseErrorMessage(event);
        });
        taQuestionText.setOnKeyPressed((event) -> {
            eraseErrorMessage(event);
        });
        tfAnswer1.setOnKeyPressed((event) -> {
            eraseErrorMessage(event);
        });
        tfAnswer2.setOnKeyPressed((event) -> {
            eraseErrorMessage(event);
        });
        tfAnswer3.setOnKeyPressed((event) -> {
            eraseErrorMessage(event);
        });
        tfAnswer4.setOnKeyPressed((event) -> {
            eraseErrorMessage(event);
        });
        btnRefresh.setOnAction((event) -> {
            handleRefreshButton(event);
        });

        cbNumOfAnswers.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldV, newV) -> SetAnswersVisibility());
    }

    private void saveCurentQuestionData() {
        currentQuestion.setText(taQuestionText.getText());
        test.setTitle(tfTestTitle.getText());

        ArrayList<Answer> newAnswers = new ArrayList<>();
        if (cbOneCorrectAnswer.isSelected()) {
            newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, tfAnswer1.getText(), rb1.isSelected()));
            newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, tfAnswer2.getText(), rb2.isSelected()));
            if (cbNumOfAnswers.getSelectionModel().getSelectedItem() > 2) {
                newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, tfAnswer3.getText(), rb3.isSelected()));
            }
            if (cbNumOfAnswers.getSelectionModel().getSelectedItem() > 3) {
                newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, tfAnswer4.getText(), rb4.isSelected()));
            }
        } else {
            newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, tfAnswer1.getText(), cb1.isSelected()));
            newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, tfAnswer2.getText(), cb2.isSelected()));
            if (cbNumOfAnswers.getSelectionModel().getSelectedItem() > 2) {
                newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, tfAnswer3.getText(), cb3.isSelected()));
            }
            if (cbNumOfAnswers.getSelectionModel().getSelectedItem() > 3) {
                newAnswers.add(new Answer(randomUUID().toString(), currentQuestion, tfAnswer4.getText(), cb4.isSelected()));
            }
        }

        Session session = Service.getSessionFactory().openSession();

        Transaction tx = session.beginTransaction();
        List<Answer> answers = currentQuestion.getAnswers();
        for (Answer a : answers) {
            Answer tempAns = (Answer) session.get(Answer.class, a.getId());
            if (tempAns != null) {
                session.delete(tempAns);
            }
        }
        tx.commit();
        session.close();

        currentQuestion.setAnswers(newAnswers);
    }

    private void initCurrentQuestion() {
        if (test.getQuestions().size() <= currentQuestionNumber) {
            addNewQuestionToTest();
        }
        currentQuestion = test.getQuestions().get(currentQuestionNumber);
    }

    private void addNewQuestionToTest() {
        Question addedQuestion = new Question();
        test.getQuestions().add(addedQuestion);
        addedQuestion.setTest(test);
        addedQuestion.setId(randomUUID().toString());
        addedQuestion.setDeleted(false);
        for (int i = 0; i < cbNumOfAnswers.getSelectionModel().getSelectedItem(); i++) {
            Answer ans = new Answer();
            ans.setId(randomUUID().toString());
            ans.setQuestion(addedQuestion);
            addedQuestion.getAnswers().add(ans);
        }
    }

    private void setAllQuestionFieldsEmpty() {
        taQuestionText.setText("");
        tfAnswer1.setText("");
        tfAnswer2.setText("");
        tfAnswer3.setText("");
        tfAnswer4.setText("");
        rb1.setSelected(false);
        rb2.setSelected(false);
        rb3.setSelected(false);
        rb4.setSelected(false);
        cb1.setSelected(false);
        cb2.setSelected(false);
        cb3.setSelected(false);
        cb4.setSelected(false);
    }

    private void populateSubjectsChoiceBox() {
        List<Subject> subjects = Service.getInstane().getSubjects();
        ArrayList<String> subjectNames = new ArrayList<String>();
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
        rb1.setVisible(true);

        cb2.setVisible(false);
        rb2.setVisible(true);

        rb3.setVisible(false);
        rb4.setVisible(false);

        if (cbNumOfAnswers.getValue().intValue() > 2) {
            cb3.setVisible(false);
            rb3.setVisible(true);
        }
        if (cbNumOfAnswers.getValue().intValue() > 3) {
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

        if (cbNumOfAnswers.getValue().intValue() > 2) {
            rb3.setVisible(false);
            cb3.setVisible(true);
        }
        if (cbNumOfAnswers.getValue().intValue() > 3) {
            rb4.setVisible(false);
            cb4.setVisible(true);
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
        tfAnswer3.setVisible(false);
        tfAnswer4.setVisible(false);

        if (cbNumOfAnswers.getValue().intValue() > 2) {
            tfAnswer3.setVisible(true);
        }
        if (cbNumOfAnswers.getValue().intValue() > 3) {
            tfAnswer4.setVisible(true);
        }
    }
}
