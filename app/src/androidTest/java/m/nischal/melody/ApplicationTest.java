package m.nischal.melody;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import m.nischal.melody.ui.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {

    @Rule
    private ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void test() {
        onView(withId(android.R.id.home)).perform(click());
    }

}