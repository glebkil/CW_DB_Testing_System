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
        
        StageConductor.getInstance().setPrimaryScene();

    }

    public void LogOut() throws Exception {
        setCurrentUser(null);
        
        StageConductor.getInstance().setPrimaryScene();
    }
}
