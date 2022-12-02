package edu.usc.eventme;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.checkerframework.checker.units.qual.A;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.google.firebase.auth.FirebaseAuth;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    private View decorView;

    @Rule public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        activityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<LoginActivity>() {
            @Override
            public void perform(LoginActivity activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });
    }

    @Test
    public void Test_successfullyLogin() throws InterruptedException {

        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("mengfeiq@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText("fangqin"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("Sign in successfully")).inRoot(withDecorView(Matchers.not(decorView))).check(matches(isDisplayed()));
        //intended(hasComponent(RegisterActivity.class.getName()));
        Thread.sleep(5000);
    }

    @Test
    public void Test_emptyInputLogin() throws InterruptedException {
        String expectedWarning = "Cannnot be empty";
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText(expectedWarning)).inRoot(withDecorView(Matchers.not(decorView))).check(matches(isDisplayed()));
        Thread.sleep(5000);
    }

    @After
    public void tearDown() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
    }

}
