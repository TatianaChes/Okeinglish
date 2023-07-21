package com.example.myapplicationtest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.myapplicationtest.ui.home.MenuActivity;

import org.junit.Rule;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityTestRule<MenuActivity> activityActivityTestRule = new ActivityTestRule<>(MenuActivity.class);
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.myapplicationtest", appContext.getPackageName());
    }
    @Test
    public void clickButtonHome() {
        onView(withId(R.id.navigation_home)).perform(click()).check(matches(isDisplayed()));
    }
    @Test
    public void clickButtonTexts() {
        onView(withId(R.id.navigation_texts)).perform(click()).check(matches(isDisplayed()));
    }
    @Test
    public void clickButtonRules() {
        onView(withId(R.id.navigation_rules)).perform(click()).check(matches(isDisplayed()));
    }
    @Test
    public void clickButtonProfile() {
        onView(withId(R.id.navigation_profile)).perform(click());
    }

}