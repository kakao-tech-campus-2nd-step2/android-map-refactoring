package campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.view.MainActivity
import org.junit.Rule
import org.junit.Test


class MapActivityUITest {

    @get:Rule
    val mapActivityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testSearchViewIsDisplayed() {
        onView(withId(R.id.main_search)).check(matches(isDisplayed()))
    }
}
