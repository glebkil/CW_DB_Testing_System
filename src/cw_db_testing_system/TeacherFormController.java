/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import Model.Test;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import utils.Seance;

/**
 * FXML Controller class
 *
 * @author Gleb
 */
public class TeacherFormController implements Initializable{
    
        
    @FXML
    public void handleBtnLogout() throws Exception {
        Seance.getInstance().LogOut();
    }
    
    @FXML
    public void handleBtnAddNewTest(){        
        StageConductor.getInstance().BuildStage("TestBuilder.fxml");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
