/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

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
import utils.Seance;
import utils.Service;
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

    ObservableList<TestDataObserver> observableTests;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Table
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
    private void handleBtnDeleteSelected(){
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
