package edu.usc.eventme;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicInteger;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventUnregisterUserTest {

    private Event testEventExist = new Event("19", "title2", "category2", "2022-12-27", "2022-12-20", "18:00", "17:00",
            2, "description2", "cost2", true, "sponsor2", "url2", "loc2");

    @Rule public ActivityScenarioRule<EventRegisterActivity> activityScenarioRule
            = new ActivityScenarioRule<>(
            new Intent(
                    InstrumentationRegistry.getInstrumentation().getTargetContext(),
                    EventRegisterActivity.class)
                    .putExtra("Event",testEventExist)
    );

    @BeforeClass
    public static void setUp() throws InterruptedException {
        final String email = "mengfeiq@usc.edu";
        final String password = "fangqin";
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
        Thread.sleep(1000);
    }

    @Test
    public void Test_UserEventUnregister() throws InterruptedException {
        AtomicInteger expected = new AtomicInteger();
        activityScenarioRule.getScenario().onActivity(activity -> {
            TextView temp = activity.findViewById(R.id.numRegistered);
            String numUser = (String)temp.getText();
            String numberOnly= numUser.replaceAll("[^0-9]", "");
            Integer num = Integer.parseInt(numberOnly);
            expected.set(num - 1);
        });
        int expectedNum = expected.get();
        onView(withId(R.id.eventRegisterButton)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.numRegistered)).check(matches(withText(expectedNum + " People Registered")));
        onView(withId(R.id.eventRegisterButton)).check(matches(withText("Register")));
        onView(withId(R.id.eventRegisterButton)).perform(click());
    }

    @AfterClass
    public static void tearDown() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
    }

}
