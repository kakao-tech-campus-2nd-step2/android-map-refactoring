package campus.tech.kakao.map

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
@MediumTest
class SearchActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(SearchActivity::class.java)

    @Before
    fun setup() {
        activityScenarioRule.scenario.onActivity { activity ->
            (activity.application as KyleMaps).isTestMode = true
        }
    }

    @Test
    fun testSearchFunctionality() {
        onView(withId(R.id.no_results))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testViewVisibility() {
        onView(withId(R.id.search_edit_text)).perform(typeText("Hello"))
        Thread.sleep(1000)
        onView(withId(R.id.no_results)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testResultRecyclerViewItem() {
        val resultRecyclerViewMatcher = ResultRecyclerViewMatcher()
        onView(withId(R.id.search_edit_text)).perform(typeText("Hello"))
        Thread.sleep(1000)
        onView(withId(R.id.recycler_view))
            .check(matches(resultRecyclerViewMatcher.atPosition(
                0,
                resultRecyclerViewMatcher.hasTextInViewWithId(R.id.place_name, "헬로우")
            )))
        onView(withId(R.id.recycler_view))
            .check(matches(resultRecyclerViewMatcher.atPosition(
                0,
                resultRecyclerViewMatcher.hasTextInViewWithId(R.id.place_category, "음식점")
            )))
        onView(withId(R.id.recycler_view))
            .check(matches(resultRecyclerViewMatcher.atPosition(
                0,
                resultRecyclerViewMatcher.hasTextInViewWithId(R.id.place_address, "서울 강남구 신사동 541-2")
            )))
    }

    @SuppressLint("CheckResult")
    @Test
    fun testSearchHistoryRecyclerViewItem() {
        val searchHistoryRecyclerViewMatcher = SearchHistoryRecyclerViewMatcher()
        var itemCount = 0
        activityScenarioRule.scenario.onActivity { activity ->
            val recyclerView: RecyclerView = activity.findViewById(R.id.horizontal_recycler_view)
            itemCount = recyclerView.adapter?.itemCount ?: 0
        }
        // 검색 입력 필드에 텍스트 입력
        onView(withId(R.id.search_edit_text)).perform(typeText("DDDD"))
        Thread.sleep(1000)

        // 특정 아이템 클릭
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(1000)

        // 특정 아이템의 텍스트 검증
        onView(withId(R.id.horizontal_recycler_view))
            .check(matches(searchHistoryRecyclerViewMatcher.atPosition(
                itemCount-1,
                searchHistoryRecyclerViewMatcher.hasTextInViewWithId(R.id.search_history_item, "DDDD")
            )))
    }

    @Test
    fun testBackButtonFunctionality() {
        // Perform a click on the back button
        onView(withId(R.id.back_button))
            .perform(click())

        // Verify that we are navigated back to the MapActivity
        // This check depends on how you handle activity transitions, adjust accordingly
        // Example, you may need to check for a specific view in MapActivity
        onView(withId(R.id.map_view)) // Change this to a view specific to MapActivity
            .check(matches(isDisplayed()))
    }
}
