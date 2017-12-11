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
import javafx.fxml.FXMLLoader;
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
        FXMLLoader loader = StageConductor.getInstance().BuildStage("/cw_db_testing_system/TestBuilder.fxml", "Adding Test");
        TestBuilderController controller = loader.getController();
        controller.setTest(new Test());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
