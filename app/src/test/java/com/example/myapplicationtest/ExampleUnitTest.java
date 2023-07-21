package com.example.myapplicationtest;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplicationtest.input.CheckEnter;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void _isCorrectName() {
        CheckEnter myTest = new CheckEnter();
        boolean result = myTest.isNameIsValid("Татьяна Иванова");
        boolean expected = true;
        assertEquals(expected, result);
    }
    @Test
    public void _isCorrectNameTwo() {
        CheckEnter myTest = new CheckEnter();
        boolean result = myTest.isNameIsValid("Жв@4ka");
        boolean expected = false;
        assertEquals(expected, result);
    }
    @Test
    public void _isCorrectLogin() {
        CheckEnter myTest = new CheckEnter();
        boolean result = myTest.checkEmail("tatianaches49@gmail.ru");
        boolean expected = true;
        assertEquals(expected, result);
    }
    @Test
    public void _isCorrectLoginTwo() {
        CheckEnter myTest = new CheckEnter();
        boolean result = myTest.checkEmail("почта.ru");
        boolean expected = false;
        assertEquals(expected, result);
    }
    @Test
    public void _isNullText() {
        CheckEnter myTest = new CheckEnter();
        boolean result = myTest.isStringNull("");
        boolean expected = true;
        assertEquals(expected, result);
    }
    @Test
    public void _isCorrectPassword() {
        CheckEnter myTest = new CheckEnter();
        boolean result = myTest.isNameIsValid("123");
        boolean expected = false;
        assertEquals(expected, result);
    }
}