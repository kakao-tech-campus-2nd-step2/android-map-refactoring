package campus.tech.kakao.map.view.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import campus.tech.kakao.map.R
import campus.tech.kakao.map.view.search.SearchActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivityUITest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MapActivity> =
        ActivityScenarioRule(MapActivity::class.java)

    @get:Rule
    var grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Test
    fun 지도_화면이_표시된다() {
        onView(withId(R.id.map_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun 검색_아이콘이_표시된다() {
        onView(withId(R.id.iv_search))
            .check(matches(isDisplayed()))
    }

    @Test
    fun 검색어를_입력해_주세요_안내가_표시된다() {
        onView(withId(R.id.tv_search_hint))
            .check(matches(isDisplayed()))
            .check(matches(withText("검색어를 입력해 주세요.")))
    }

    @Test
    fun 검색_아이콘_클릭시_SearchActivity가_시작된다() {
        Intents.init()

        onView(withId(R.id.iv_search)).perform(click())

        intended(hasComponent(SearchActivity::class.java.name))

        Intents.release()
    }
}
