/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import Model.PassedTest;
import Model.Question;
import Model.Test;
import Model.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import utils.PassedTestDataObserver;
import utils.Seance;
import utils.Service;
import utils.TestDataObserver;

/**
 * FXML Controller class
 *
 * @author Gleb
 */
public class StudentFormController implements Initializable {

    @FXML
    private TableView<TestDataObserver> tvTestsTable;
    
    @FXML
    private TableView<PassedTestDataObserver> tvptTestsTable;

    @FXML
    private TableColumn<TestDataObserver, String> tcSubject, tcTitle;

    @FXML
    private TableColumn<PassedTestDataObserver, String> tcptSubject, tcptTitle, tcptDateTime;

    @FXML
    private TableColumn<PassedTestDataObserver, Double> tcptMark;
    
    @FXML
    private Button btnStartTest, btnSeeTestStats, btnRefresh;

    @FXML
    private void handleBtnLogout(ActionEvent e) throws Exception {
        Seance.getInstance().LogOut();
    }

    @FXML
    private void handleBtnRefresh() {
        List<Test> tests = Service.getInstane().getTests();
        observableTests.clear();
        for (Test t : tests) {
            observableTests.add(new TestDataObserver(t));
        }

        Session session = Service.getSessionFactory().openSession();
        User usr = (User) session.get(User.class, Seance.getInstance().getCurrentUser().getId());
        Hibernate.initialize(usr.getPassedTests());
        session.close();

        List<PassedTest> passedTests = usr.getPassedTests();
        observablePassedTests.clear();
        for (PassedTest pt : passedTests) {
            observablePassedTests.add(new PassedTestDataObserver(pt));
        }
    }

    @FXML
    private void handleBtnSeetestStats() {
        PassedTestDataObserver ptdo = tvptTestsTable.getSelectionModel().getSelectedItem();
        if (ptdo == null) {
            return;
        }
        Session session = Service.getSessionFactory().openSession();
        PassedTest selectedPassedTest = (PassedTest) session.get(PassedTest.class, ptdo.getId());

        session.close();
        List<PassedTest> ptl = new ArrayList<>();
        ptl.add(selectedPassedTest);
        StageConductor.getInstance().BuildSceneOnNewStage("testStats.fxml",
                "Test stat", new TestStatsController(ptl));
    }

    @FXML
    private void handleBtnStartTest() {
        TestDataObserver tdo = tvTestsTable.getSelectionModel().getSelectedItem();
        if (tdo == null) {
            return;
        }
        Session session = Service.getSessionFactory().openSession();
        Test selectedTest = (Test) session.get(Test.class, tdo.getId());
        Hibernate.initialize(selectedTest.getPassedTests());
        Hibernate.initialize(selectedTest.getQuestions());

        List<Question> ql = selectedTest.getQuestions();
        for (Question q : ql) {
            Hibernate.initialize(q.getAnswers());
        }
        session.close();
        StageConductor
                .getInstance()
                .BuildSceneOnNewStage("Testing.fxml", "Testing", new TestingController(selectedTest));
    }

    ObservableList<TestDataObserver> observableTests;
    ObservableList<PassedTestDataObserver> observablePassedTests;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Init table of tests
        observableTests = FXCollections.observableArrayList();
        tcSubject.setCellValueFactory(new PropertyValueFactory<>("testSubject"));
        tcTitle.setCellValueFactory(new PropertyValueFactory<>("testTitle"));

        List<Test> tests = Service.getInstane().getTests();
        for (Test t : tests) {
            observableTests.add(new TestDataObserver(t));
        }
        tvTestsTable.setItems(observableTests);
        tvTestsTable.getColumns().clear();
        tvTestsTable.getColumns().addAll(tcSubject, tcTitle);

        //Init table of passed tests
        observablePassedTests = FXCollections.observableArrayList();
        tcptSubject.setCellValueFactory(new PropertyValueFactory<>("passedTestSubject"));
        tcptTitle.setCellValueFactory(new PropertyValueFactory<>("passedTestTitle"));
        tcptDateTime.setCellValueFactory(new PropertyValueFactory<>("passedTestDateTime"));
        tcptMark.setCellValueFactory(new PropertyValueFactory<>("passedTestMark"));

        Session session = Service.getSessionFactory().openSession();
        User usr = (User) session.get(User.class, Seance.getInstance().getCurrentUser().getId());
        Hibernate.initialize(usr.getPassedTests());
        session.close();

        List<PassedTest> passedTests = usr.getPassedTests();
        for (PassedTest pt : passedTests) {
            observablePassedTests.add(new PassedTestDataObserver(pt));
        }
        tvptTestsTable.setItems(observablePassedTests);
        tvptTestsTable.getColumns().clear();
        tvptTestsTable.getColumns().addAll(tcptSubject, tcptTitle, tcptDateTime, tcptMark);
    }

}
