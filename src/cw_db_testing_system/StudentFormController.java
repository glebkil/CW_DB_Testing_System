/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import utils.Seance;

/**
 * FXML Controller class
 *
 * @author Gleb
 */
public class StudentFormController implements Initializable {

    
    @FXML
    public void handlerLogoutBtn(ActionEvent e) throws Exception{
        Seance.getInstance().LogOut();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
