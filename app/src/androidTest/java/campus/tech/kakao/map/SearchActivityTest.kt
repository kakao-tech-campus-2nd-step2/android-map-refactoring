@file:Suppress("DEPRECATION")

package campus.tech.kakao.map

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import campus.tech.kakao.map.View.Map_Activity
import campus.tech.kakao.map.View.Search_Activity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(Search_Activity::class.java)

    @Test
    fun testSearchView_isDisplayed() {
        onView(withId(R.id.search_text)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchRecyclerView_isDisplayed() {
        onView(withId(R.id.RecyclerVer)).check(matches(isDisplayed()))
    }

    @Test
    fun testSavedSearchRecyclerView_isDisplayed() {
        onView(withId(R.id.recyclerHor)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchView_textEntry() {
        onView(withId(R.id.search_text))
            .perform(typeText("카페"), pressImeActionButton())

        onView(withId(R.id.RecyclerVer)).check(matches(hasDescendant(withText("Cafe Name"))))
    }

    @Test
    fun testSearchResult_click() {
        onView(withId(R.id.search_text))
            .perform(typeText("도서관"), pressImeActionButton())

        Thread.sleep(2000)
        //recyclerver에 담긴 데이터를 클릭하면 그 데이터 위치로 이동하고 서칭을 해야하는데, actionOnItemAtPosition함수를 어떻게 구현해야할지 모르겠습니다.
    }

}
