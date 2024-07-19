package campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchPlaceActivityUITEST {

    @get:Rule
    val activityRule = ActivityTestRule(SearchPlaceActivity::class.java)

    @Test
    fun UIcheck() {

        onView(withId(R.id.search))
            .check(matches(isDisplayed()))


        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))


        onView(withId(R.id.saved_search))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testSearchFunctionality() {

        onView(withId(R.id.search))
            .perform(typeText("카페"), closeSoftKeyboard())

        // Check if the results RecyclerView is populated with items (assuming we have some items)
        onView(withId(R.id.recyclerView))
            .check(matches(hasDescendant(withText("바다정원"))))
    }

    @Test
    fun testSavedSearchFunctionality() {

        onData(
            allOf(
                `is`(instanceOf(SavedSearch::class.java)),
                hasToString(containsString("바다정원"))
            )
        )
            .inAdapterView(withId(R.id.saved_search))
            .perform(click())


        onView(withId(R.id.search))
            .check(matches(withText("바다정원")))
    }
}
