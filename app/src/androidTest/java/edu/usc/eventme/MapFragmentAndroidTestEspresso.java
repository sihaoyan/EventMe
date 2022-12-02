package edu.usc.eventme;

import android.app.Activity;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import java.lang.Thread;
import java.io.*;
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MapFragmentAndroidTestEspresso {
    public static final String STRING_TO_BE_TYPED_100 = "100";
    public static final String STRING_TO_BE_DISPLAYED_10 = "10";

    public static final String STRING_TO_BE_TYPED_3 = "3";
    public static final String STRING_TO_BE_DISPLAYED_3 = "1";

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes.
     */
    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void openmap() throws UiObjectNotFoundException, InterruptedException {
        MapsFragment map = new MapsFragment();
        activityScenarioRule.getScenario().onActivity(activity -> {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, map);
            transaction.commit();
        });
        Thread.sleep(2000);
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Food truck for snack"));
        marker.click();
        Thread.sleep(2000);
    }
//    @Test
//    public void Test_mapdisplayed() throws UiObjectNotFoundException {
//        // Type text and then press the button.
////        MapsFragment map = new MapsFragment();
////        activityScenarioRule.getScenario().onActivity(activity -> {
////            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
////            transaction.replace(R.id.fragmentContainerView, map);
////            transaction.commit();
////        });
//        onView(withId(R.id.maplayout)).check(matches(isDisplayed()));
//
//
//        // Check that the text was changed.
//        //onView(withId(R.id.textView)).check(matches(withText(STRING_TO_BE_DISPLAYED_10)));
//    }

    @Test
    public void Test_backtolist() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.currentevent)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.backButton)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.bottom_sheet)).check(matches(isDisplayed()));
        Thread.sleep(2000);
    }

    @Test
    public void Test_Show_Eventdetail() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.currentevent)).perform(click());
        onView(withId(R.id.textView2)).check(matches(isDisplayed()));
        //onView(withText("Internet trending snack truck is coming to USC! Have you tried egg waffle with ice cream before? Please stop by and try out this trending, delicious snack and post it on Instagram!")).check(matches(isDisplayed()));
        Thread.sleep(2000);
    }

    @Test
    public void Test_Show_Bottomsheet() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.bottom_sheet)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(19));
        Thread.sleep(2000);
    }
}
