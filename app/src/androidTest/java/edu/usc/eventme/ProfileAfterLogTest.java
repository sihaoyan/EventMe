package edu.usc.eventme;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.checkerframework.checker.units.qual.A;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
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
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProfileAfterLogTest {
    final String email = "mengfeiq@usc.edu";
    final String password = "fangqin";
    private FirebaseAuth mAuth;

    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() throws InterruptedException {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            ProfileFragment profileFragment = new ProfileFragment();
                            activityScenarioRule.getScenario().onActivity(activity -> {
                                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragmentContainerView, profileFragment);
                                transaction.commit();
                            });
                        }
                    }
                });
        Thread.sleep(1000);

    }

    @Test
    public void Test_LoggedUserProfile() {
        // Then use Espresso to test the Fragment
        onView(withId(R.id.userEmail)).check(matches(withText(email)));
        onView(withId(R.id.logoutButton)).check(matches(withText("Log Out")));
    }

    @Test
    public void Test_LoggedOutFunction() throws InterruptedException {
        Thread.sleep(500);
        onView(withId(R.id.userScrollView)).perform(swipeUp());
        onView(withId(R.id.logoutButton)).perform(click());
        onView(withId(R.id.loginPage)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() {
        mAuth.signOut();
    }
}
