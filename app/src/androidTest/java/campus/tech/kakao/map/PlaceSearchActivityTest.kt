package campus.tech.kakao.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.presenter.view.adapter.SearchResultAdapter
import campus.tech.kakao.map.presenter.view.MapActivity
import campus.tech.kakao.map.presenter.view.PlaceSearchActivity
import org.hamcrest.Matchers.instanceOf
import org.junit.Rule
import org.junit.Test

class PlaceSearchActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(PlaceSearchActivity::class.java)


    @Test
    fun 검색항목_클릭시_맵으로_이동(){
        Intents.init()
        onView(ViewMatchers.withId(R.id.etSearchPlace)).perform(
            replaceText("카페")
        )

        Thread.sleep(1000)

        onView(ViewMatchers.withId(R.id.searchResult))
            .perform(
                RecyclerViewActions.actionOnHolderItem(
                    instanceOf(SearchResultAdapter.ViewHolder::class.java), click()
                ).atPosition(1)
            )


        Intents.intended(IntentMatchers.hasComponent(MapActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun 삭제버튼_클릭시_검색내용_삭제(){
        onView(ViewMatchers.withId(R.id.etSearchPlace)).perform(
            replaceText("카페")
        )

        onView(ViewMatchers.withId(R.id.deleteSearch)).perform(click())
        onView(ViewMatchers.withId(R.id.etSearchPlace)).check(matches(withText("")))
    }
}
