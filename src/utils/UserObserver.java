/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.User;

/**
 *
 * @author Gleb
 */
public class UserObserver {

    private String id;
//    private Group group;
    private String role;
    private String login;
    private String password;
    private String name;

    private Integer writtenTests;
    private Integer passedTests;
    
    public UserObserver(User usr){
        id = usr.getId();
        role = usr.getRole().getName();
        login = usr.getLogin();
        password = usr.getPassword();
        name = usr.getName();
        writtenTests = usr.getTests().size();
        passedTests = usr.getPassedTests().size();
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWrittenTests(Integer writtenTests) {
        this.writtenTests = writtenTests;
    }

    public void setPassedTests(Integer passedTests) {
        this.passedTests = passedTests;
    }

    public String getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Integer getWrittenTests() {
        return writtenTests;
    }

    public Integer getPassedTests() {
        return passedTests;
    }    
}
