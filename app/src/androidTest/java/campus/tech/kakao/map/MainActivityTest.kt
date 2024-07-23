package campus.tech.kakao.map

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import campus.tech.kakao.map.view.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @Test
    fun testUIElementsAreDisplayed() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(ViewMatchers.withId(R.id.inputText))
            onView(ViewMatchers.withId(R.id.cancelBtn))
            onView(ViewMatchers.withId(R.id.recyclerView))
            onView(ViewMatchers.withId(R.id.searchView))
        }
    }

    @Test
    fun testCancelButtonClearsInputText() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.inputText)).perform(typeText("Test Input"))
            onView(withId(R.id.cancelBtn)).perform(click())
            onView(withId(R.id.inputText)).check(matches(withText("")))
        }
    }

    @Test
    fun testSearchFunctionality() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.inputText)).perform(typeText("Test Location"))
            onView(withId(R.id.resultView)).check(matches(isDisplayed()))
        }
    }
}