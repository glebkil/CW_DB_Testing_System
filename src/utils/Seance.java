/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.User;
import cw_db_testing_system.StageConductor;

/**
 *
 * @author Gleb
 */
public class Seance {

    private User currentUser;

    private Seance() {
        currentUser = null;
    }

    private final static class SingletonHolder {

        private final static Seance instance = new Seance();
    }

    public static Seance getInstance() {
        return SingletonHolder.instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void setCurrentUser(User user) {
        currentUser = user;
    }

    public void LogIn(String login, String password) throws Exception {

        Service srvc = Service.getInstane();
        User user = srvc.getUserByLogin(login);
        Validator.validateUserPassword(user, password);
        setCurrentUser(user);
        
        if(user.getRole().getName().equals("student")){
            StageConductor.getInstance().BuildSceneOnPrimaryStage("studentForm.fxml", "Student panel");
        } else if(user.getRole().getName().equals("teacher")){
            StageConductor.getInstance().BuildSceneOnPrimaryStage("teacherForm.fxml", "Teacher panel");
        } else if(user.getRole().getName().equals("admin")){
            StageConductor.getInstance().BuildSceneOnPrimaryStage("adminForm.fxml", "Admin panel");
        }     
    }

    public void LogOut(){
        setCurrentUser(null);        
        StageConductor.getInstance().BuildSceneOnPrimaryStage("RegistrationForm.fxml", "Authorisation");
    }
}
