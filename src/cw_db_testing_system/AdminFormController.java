/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import Model.Test;
import Model.User;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.Seance;
import utils.Service;
import utils.TestDataObserver;
import utils.UserObserver;

/**
 * FXML Controller class
 *
 * @author Gleb
 */
public class AdminFormController implements Initializable {

    @FXML
    private TableView tvUsers;

    @FXML
    private TableColumn<UserObserver, String> tcLogin, tcPassword,
            tcFullName, tcRole;

    @FXML
    private TableColumn<UserObserver, Integer> tcTestsWritten, tcTestsPassed;

    @FXML
    private void handleDeleteSelectedBtn() {
        UserObserver uo = (UserObserver) tvUsers.getSelectionModel().getSelectedItem();
        if (uo == null) {
            return;
        }
        Session session = Service.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        User usrToDel = (User) session.get(User.class, uo.getId());
        usrToDel.setDeleted(true);
        session.update(usrToDel);
        tx.commit();
        session.close();
        handleRefreshBtn();
    }

    @FXML
    private void handleRefreshBtn() {
        Session session = Service.getSessionFactory().openSession();
        observableUsers.clear();
        List<User> users = Service.getInstane().getUsers();
        for (User u : users) {
             User usr = (User)session.get(User.class, u.getId());
            Hibernate.initialize(usr.getPassedTests());
            Hibernate.initialize(usr.getTests());
            observableUsers.add(new UserObserver(usr));
        }
        session.close();
    }

    @FXML
    private void handleLogOutBtn() {
        Seance.getInstance().LogOut();
    }

    ObservableList<UserObserver> observableUsers;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        observableUsers = FXCollections.observableArrayList();
        tcLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        tcFullName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        tcPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        tcTestsPassed.setCellValueFactory(new PropertyValueFactory<>("passedTests"));
        tcTestsWritten.setCellValueFactory(new PropertyValueFactory<>("writtenTests"));
        Session session = Service.getSessionFactory().openSession();
        List<User> users = Service.getInstane().getUsers();
        for (User u : users) {
            User usr = (User)session.get(User.class, u.getId());
            Hibernate.initialize(usr.getPassedTests());
            Hibernate.initialize(usr.getTests());
            observableUsers.add(new UserObserver(usr));
        }
        session.close();
        tvUsers.setItems(observableUsers);
        tvUsers.getColumns().clear();
        tvUsers.getColumns().addAll(tcLogin, tcPassword, tcFullName,
                tcRole, tcTestsWritten, tcTestsPassed);
    }

}
