/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import Model.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import utils.*;
import utils.Service;

/**
 * FXML Controller class
 *
 * @author Gleb
 */
public class RegistrationFormController implements Initializable {

    @FXML
    private TextField tfLoginForLogin;

    @FXML
    private PasswordField pfPasswordForLogin;

    @FXML
    private TextField tfFullName;

    @FXML
    private TextField tfLoginForRegistration;

    @FXML
    private PasswordField pfPasswordForRegistration;

    @FXML
    private CheckBox cbIsTeacher;

    @FXML
    private PasswordField pfTeacherCode;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private Label lbMessage;

    @FXML
    private void handleLoginBtn(ActionEvent event) {
        try {
            Seance.getInstance().LogIn(tfLoginForLogin.getText(), pfPasswordForLogin.getText());
        } catch (CustomException e) {
            lbMessage.setTextFill(Color.web("#911111"));
            lbMessage.setText(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleTeacherCheckBox(ActionEvent event) {
        if (cbIsTeacher.isSelected()) {
            pfTeacherCode.setVisible(true);
        } else {
            pfTeacherCode.setVisible(false);
        }
    }

    @FXML
    private void handleRegisterBtn(ActionEvent event) {
        String login = tfLoginForRegistration.getText();
        String password = pfPasswordForRegistration.getText();
        String fullName = tfFullName.getText();
        String roleName;
        Service srvc = Service.getInstane();
        try {
            if (cbIsTeacher.isSelected()) {
                String teacherPassword = pfTeacherCode.getText();
                Validator.validateTeacherPassword(teacherPassword);
                roleName = "teacher";
            } else {
                roleName = "student";
            }

            String uuid = srvc.AddUser(srvc.getRoleByName(roleName), login, password, fullName);
            Seance.getInstance().LogIn(login, password);
        } catch (CustomException e) {
            lbMessage.setTextFill(Color.web("#911111"));
            lbMessage.setText(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
