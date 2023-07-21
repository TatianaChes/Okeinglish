package com.example.myapplicationtest.input;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckEnter implements  ICheckEnter {
    public static String key = "password";
    public static boolean update = true;

    public boolean isNameIsValid(String name) {
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile ("[!@#$:%&*()_+=|<>?{}\\[\\]~]");
        Matcher hasDigit = digit.matcher(name);
        Matcher hasSpecial = special.matcher(name);
        return !(hasDigit.find() || hasSpecial.find());
    }

   public boolean isStringNull(String string) {
        return  (string.equals("") || string.length() == 0);
    }
    public boolean checkEmail(String email) {
        return  (email.contains("@") && email.contains("."));
    }
    public boolean checkPassword(String password) {
       return !(password.length() < 6) ;
    }
}
