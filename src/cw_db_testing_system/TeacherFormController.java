/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import Model.PassedTest;
import Model.Question;
import Model.Subject;
import Model.Test;
import Model.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.PassedTestDataObserver;
import utils.Seance;
import utils.Service;
import utils.StudentsObserver;
import utils.TestDataObserver;

/**
 * FXML Controller class
 *
 * @author Gleb
 */
public class TeacherFormController implements Initializable {

    @FXML
    private TableView<TestDataObserver> tvTestsTable;

    @FXML
    private TableColumn<TestDataObserver, String> tcSubject;

    @FXML
    private TableColumn<TestDataObserver, String> tcTitle;

    @FXML
    private Button btnRefresh;

    @FXML
    private TableView<StudentsObserver> tvStat;

    @FXML
    private TableColumn<StudentsObserver, String> tcName, tcLogin;

    @FXML
    private TableColumn<StudentsObserver, Double> tcAvgMark, tcMinMark, tcMaxMark;

    @FXML 
    private TableColumn<StudentsObserver, Integer> tcTotalTestsPassed;        
    
    ObservableList<StudentsObserver> observableStudents;
    ObservableList<TestDataObserver> observableTests;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Tests Table
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
        
        //Student stat table
        observableStudents = FXCollections.observableArrayList();
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        tcAvgMark.setCellValueFactory(new PropertyValueFactory<>("avgmark"));
        tcMinMark.setCellValueFactory(new PropertyValueFactory<>("minMark"));
        tcMaxMark.setCellValueFactory(new PropertyValueFactory<>("maxMark"));
        tcTotalTestsPassed.setCellValueFactory(new PropertyValueFactory<>("totalTestsPassed"));
        
        List<User> students = Service.getInstane().getStudents();
        
        for (User st : students) {
            observableStudents.add(new StudentsObserver(st));
        }
        tvStat.setItems(observableStudents);
        tvStat.getColumns().clear();
        tvStat.getColumns().addAll(tcName, tcLogin, tcAvgMark,tcMinMark,tcMaxMark,tcTotalTestsPassed);
    }

    @FXML
    private void handleBtnFullStat() {
        StudentsObserver sto = tvStat.getSelectionModel().getSelectedItem();
        if (sto == null) {
            return;
        }
        Session session = Service.getSessionFactory().openSession();
        User selectedStudent = (User) session.get(User.class, sto.getId());
        Hibernate.initialize(selectedStudent.getPassedTests());
        
        session.close();
        
        StageConductor.getInstance().BuildSceneOnNewStage("testStats.fxml",
                "Test stat", new TestStatsController(selectedStudent.getPassedTests()));
    }

    @FXML
    void handleBtnRefreshStats() {
        List<User> students = Service.getInstane().getStudents();
        observableStudents.clear();
        for (User st : students) {
            observableStudents.add(new StudentsObserver(st));
        }
    }

    @FXML
    private void handleBtnLogout() {
        Seance.getInstance().LogOut();
    }

    @FXML
    private void handleBtnAddNewTest() {
        StageConductor
                .getInstance()
                .BuildSceneOnNewStage("/cw_db_testing_system/TestBuilder.fxml", "Adding Test", new TestBuilderController(new Test()));

    }

    @FXML
    private void handleBtnDeleteSelected() {
        TestDataObserver tdo = tvTestsTable.getSelectionModel().getSelectedItem();
        if (tdo == null) {
            return;
        }
        Session session = Service.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Test selectedTest = (Test) session.get(Test.class, tdo.getId());
//        Hibernate.initialize(selectedTest.getPassedTests());
//        Hibernate.initialize(selectedTest.getQuestions());
//
//        List<Question> ql = selectedTest.getQuestions();
//        for (Question q : ql) {
//            Hibernate.initialize(q.getAnswers());
//        }
        selectedTest.setDeleted(true);
        session.update(selectedTest);
        tx.commit();
        session.close();
        handleBtnRefresh();
    }

    @FXML
    private void handleBtnEditSelected() {
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
                .BuildSceneOnNewStage("/cw_db_testing_system/TestBuilder.fxml", "Adding Test", new TestBuilderController(selectedTest));

    }

    @FXML
    private void handleBtnRefresh() {
        List<Test> tests = Service.getInstane().getTests();
        observableTests.clear();
        for (Test t : tests) {
            observableTests.add(new TestDataObserver(t));
        }
    }
}
