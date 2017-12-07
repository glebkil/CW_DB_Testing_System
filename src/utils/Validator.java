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
public class Validator {

    private static boolean validateStringLength(String str, int minLen, int maxLen) {
        return (str.length() >= minLen && str.length() <= maxLen);
    }

    public static void validatePasswordLength(String password) throws CustomException {
        if (!validateStringLength(password, Consts.getMinPasswordLength(), Consts.getMaxPasswordLength())) {
            throw new CustomException("Invalid password length");
        }
    }

    public static void validateLoginLength(String login) throws CustomException {
        if (!validateStringLength(login, Consts.getMinLoginLength(), Consts.getMaxLoginLength())) {
            throw new CustomException("Invalid login length");
        }
    }

    public static void validateFullNameLength(String fullName) throws CustomException {
        if (!validateStringLength(fullName, Consts.getMinFullNameLength(), Consts.getMaxFullNameLength())) {
            throw new CustomException("Invalid full name length");
        }
    }

    public static void validateTeacherPassword(String password) throws CustomException {
        if (!password.equals(Consts.getTeacherPassword())) {
            throw new CustomException("Invalid teacher code");
        }
    }

    public static void validateUserPassword(User user, String password) throws CustomException {
        if (user == null) {
            throw new CustomException("Invalid login or password, try again");
        }
        if (!(user.getPassword().equals(password))) {
            throw new CustomException("Invalid login or password, try again");
        }
    }

    public static void validateSubjectLength(String subject) throws CustomException{
        if (!validateStringLength(subject, Consts.getMinSubjectLength(), Consts.getMaxSubjectLength())) {
            throw new CustomException("Invalid subject length");
        }
    }
}
