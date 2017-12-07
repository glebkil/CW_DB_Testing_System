/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.CustomException;
import utils.Service;

/**
 * FXML Controller class
 *
 * @author Gleb
 */
public class AddingSubjectController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML 
    private AnchorPane ap;
    
    @FXML
    private TextField tfSubjectTitle;

    @FXML
    private Label lbError;

    @FXML
    public void handleAddBtn() {
        if (!tfSubjectTitle.getText().isEmpty()) {

            try {
                Service.getInstane().AddSubject(tfSubjectTitle.getText());
                Stage st = (Stage) ap.getScene().getWindow();
                st.close();
            } catch (CustomException e) {
                lbError.setTextFill(Color.web("#911111"));
                lbError.setText(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
