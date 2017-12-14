/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cw_db_testing_system;

import Model.PassedTest;
import Model.Test;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author Gleb
 */
public class TestStatsController implements Initializable {

    @FXML
    private TextArea taTestStats;

    PassedTest passedTest;

    public TestStatsController(PassedTest pt) {
        passedTest = pt;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        taTestStats.setText(formString());
    }

    private String formString() {
        Test test = passedTest.getTest();
        String out = new String("Test \"" + test.getTitle() + "\"\n On "
                + test.getSubject().getTitle() + " subject\n"
                + "Passed by " + passedTest.getUser().getName() + "\n"
                + "On "
                + passedTest.getTestDate().toString()
                + "\nScore is " + passedTest.getMark()
                + " out of 100");
        return out;
    }
}
