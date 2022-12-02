package edu.usc.eventme;

import android.app.Activity;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.Root;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
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
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import java.lang.Thread;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExploreFragmetnAndroidTestEspresso {
    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void openexplore() throws UiObjectNotFoundException, InterruptedException {
        ExploreFragment exp = new ExploreFragment();
        activityScenarioRule.getScenario().onActivity(activity -> {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, exp);
            transaction.commit();
        });
        Thread.sleep(2000);
    }

    @Test
    public void Test_FoodCategory() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.image_food)).perform(click());
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.recyclerView))
                // scrollTo will fail the test if no item matches.
                .perform(RecyclerViewActions.scrollTo(
                        ViewMatchers.hasDescendant(withText("Food truck for snack"))
                ));
        onView(withText("Food truck for snack")).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(5));
    }

    @Test
    public void Test_ArtCategory() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.image_art)).perform(click());
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.recyclerView))
                // scrollTo will fail the test if no item matches.
                .perform(RecyclerViewActions.scrollTo(
                        ViewMatchers.hasDescendant(withText("Chloë Bass: Wayfinding"))
                ));
        onView(withText("Chloë Bass: Wayfinding")).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(5));
    }

    @Test
    public void Test_MusicCategory() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.image_music)).perform(click());
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.recyclerView))
                // scrollTo will fail the test if no item matches.
                .perform(RecyclerViewActions.scrollTo(
                        ViewMatchers.hasDescendant(withText("Famous DJ coming to LA"))
                ));
        onView(withText("Famous DJ coming to LA")).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(5));
    }

    @Test
    public void Test_OutdoorCategory() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.image_outdoor)).perform(click());
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.recyclerView))
                // scrollTo will fail the test if no item matches.
                .perform(RecyclerViewActions.scrollTo(
                        ViewMatchers.hasDescendant(withText("Join Surfing Team"))
                ));
        onView(withText("Join Surfing Team")).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(5));
    }

    @Test
    public void Test_SearchbyTitle() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.searchView)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.searchView)).perform(typeText("Food truck for snack"),pressKey(
                KeyEvent.KEYCODE_ENTER));
        Thread.sleep(2000);

        onView(withText("Food truck for snack")).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(1));
    }

    @Test
    public void Test_SearchbyDate() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.startDate)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.startDate)).perform(typeText("2023-04-30"),pressKey(
                KeyEvent.KEYCODE_ENTER));
        Thread.sleep(2000);
        onView(withId(R.id.endDate)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.endDate)).perform(typeText("2023-05-30"),pressKey(
                KeyEvent.KEYCODE_ENTER));
        Thread.sleep(2000);
        onView(withId(R.id.button2)).perform(click());
        Thread.sleep(2000);
        onView(withText("New Hiking Trail")).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(1));
    }

    @Test
    public void Test_SearchbyDate_wrongForm() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.startDate)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.startDate)).perform(typeText("2023/04/30"),pressKey(
                KeyEvent.KEYCODE_ENTER));
        Thread.sleep(2000);
        onView(withId(R.id.endDate)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.endDate)).perform(typeText("2023-15-40"),pressKey(
                KeyEvent.KEYCODE_ENTER));
        Thread.sleep(2000);
        onView(withId(R.id.button2)).perform(click());
        Thread.sleep(2000);
        onView(withText("Please enter correct date format!")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    public class ToastMatcher extends TypeSafeMatcher<Root> {
        @Override    public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    //means this window isn't contained by any other windows.
                    return true;
                }
            }
            return false;
        }

        @Override
        public void describeTo(org.hamcrest.Description description) {
            description.appendText("is toast");
        }
    }

    @Test
    public void Test_SearchbyDate_Reverse() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.startDate)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.startDate)).perform(typeText("2023-04-30"),pressKey(
                KeyEvent.KEYCODE_ENTER));
        Thread.sleep(2000);
        onView(withId(R.id.endDate)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.endDate)).perform(typeText("2023-04-01"),pressKey(
                KeyEvent.KEYCODE_ENTER));
        Thread.sleep(2000);
        onView(withId(R.id.button2)).perform(click());
        Thread.sleep(2000);
        // onView(withText()).check(matches(isDisplayed()));
        onView(withText("The start date should be earlier than the end date")).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void Test_SortByDis() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.image_food)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(5));
        Thread.sleep(1000);
        onView(withId(R.id.autoCompleteTextView)).perform(click());
        onView(withText("distance"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        Thread.sleep(1000);
        onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition(2));
        Thread.sleep(1000);
        onView(withText("Italian Food Free Taste: Pasta Roma")).check(matches(isDisplayed()));
        Thread.sleep(1000);
    }

    @Test
    public void Test_SortByDate() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.image_food)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(5));
        Thread.sleep(1000);
        onView(withId(R.id.autoCompleteTextView)).perform(click());
        onView(withText("date"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        Thread.sleep(1000);
        onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition(2));
        Thread.sleep(1000);
        onView(withText("Asian Food Night Market")).check(matches(isDisplayed()));
        Thread.sleep(1000);
    }

    @Test
    public void Test_SortByAlpha() throws UiObjectNotFoundException, InterruptedException {
        Thread.sleep(3000);
        onView(withId(R.id.image_food)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(5));
        Thread.sleep(1000);
        onView(withId(R.id.autoCompleteTextView)).perform(click());
        onView(withText("alphabet"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        Thread.sleep(1000);
        onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToPosition(2));
        Thread.sleep(1000);
        onView(withText("Eat Breakfast Everyday!")).check(matches(isDisplayed()));
        Thread.sleep(1000);
    }

}
