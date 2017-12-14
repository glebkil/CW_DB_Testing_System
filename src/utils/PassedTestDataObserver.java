/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.PassedTest;

/**
 *
 * @author Gleb
 */
public class PassedTestDataObserver {

    private String passedTestTitle;
    private String passedTestSubject;
    private String passedTestDateTime;
    private String id;
    private Double passedTestMark;

    public PassedTestDataObserver(PassedTest test) {
        passedTestTitle = test.getTest().getTitle();
        passedTestSubject = test.getTest().getSubject().getTitle();
        id = test.getId();
        passedTestDateTime = test.getTestDate().toString();
        passedTestMark = test.getMark();
    }

    public void setPassedTestTitle(String passedTestTitle) {
        this.passedTestTitle = passedTestTitle;
    }

    public void setPassedTestSubject(String passedTestSubject) {
        this.passedTestSubject = passedTestSubject;
    }

    public void setPassedTestDateTime(String passedTestDateTime) {
        this.passedTestDateTime = passedTestDateTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassedTestTitle() {
        return passedTestTitle;
    }

    public String getPassedTestSubject() {
        return passedTestSubject;
    }

    public String getPassedTestDateTime() {
        return passedTestDateTime;
    }

    public String getId() {
        return id;
    }

    public void setPassedTestMark(Double passedTestMark) {
        this.passedTestMark = passedTestMark;
    }

    public Double getPassedTestMark() {
        return passedTestMark;
    }

}
