/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Test;

/**
 *
 * @author Gleb
 */
//Used for observed lists in table view
 public class TestDataObserver {

        private String testTitle;
        private String testSubject;
        private String id;

        public TestDataObserver(Test test) {
            testTitle = test.getTitle();
            testSubject = test.getSubject().getTitle();
            id = test.getId();
        }

        public String getTestTitle() {
            return testTitle;
        }

        public String getTestSubject() {
            return testSubject;
        }

        public String getId() {
            return id;
        }

        public void setTestTitle(String testTitle) {
            this.testTitle = testTitle;
        }

        public void setTestSubject(String testSubject) {
            this.testSubject = testSubject;
        }

    }
