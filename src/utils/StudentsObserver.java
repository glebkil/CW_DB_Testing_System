/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.PassedTest;
import Model.User;
import java.util.List;

/**
 *
 * @author Gleb
 */
public class StudentsObserver {

    private String name;
    private String login;
    private Double avgMark;
    private Double minMark;
    private Double maxMark;
    private Integer totalTestsPassed;
    private String id;

    public StudentsObserver(User usr) {
        name = usr.getName();
        login = usr.getLogin();

        Double average = 0.0;
        Double max = 0.0;
        Double min = 100.0;
        List<PassedTest> passedtests = usr.getPassedTests();
        for (PassedTest pt : passedtests) {
            average += pt.getMark();
            if (pt.getMark() > max) {
                max = pt.getMark();
            }
            if (pt.getMark() < min) {
                min = pt.getMark();
            }
        }
        if (passedtests.size() != 0) {
            average /= passedtests.size();
            avgMark = average;
            minMark = min;
            maxMark = max;
        }

        totalTestsPassed = passedtests.size();
        id = usr.getId();
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setAvgMark(Double avgMark) {
        this.avgMark = avgMark;
    }

    public void setMinMark(Double minMark) {
        this.minMark = minMark;
    }

    public void setMaxMark(Double maxMark) {
        this.maxMark = maxMark;
    }

    public void setTotalTestsPassed(Integer totalTestsPassed) {
        this.totalTestsPassed = totalTestsPassed;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public Double getAvgMark() {
        return avgMark;
    }

    public Double getMinMark() {
        return minMark;
    }

    public Double getMaxMark() {
        return maxMark;
    }

    public Integer getTotalTestsPassed() {
        return totalTestsPassed;
    }
}
