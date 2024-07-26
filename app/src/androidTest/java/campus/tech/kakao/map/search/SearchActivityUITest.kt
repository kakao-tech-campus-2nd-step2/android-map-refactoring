package campus.tech.kakao.map.view.search

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchActivityUITest {

    @get:Rule
    var activityRule: ActivityScenarioRule<SearchActivity> =
        ActivityScenarioRule(SearchActivity::class.java)

    @Test
    fun 검색어_cafe를_입력하면_cafe가_입력된다() {
        onView(withId(R.id.et_search_text_input))
            .perform(typeText("cafe"), closeSoftKeyboard())
            .check(matches(withText("cafe")))
    }

    @Test
    fun 검색어를_입력하고_검색어_삭제_아이콘을_누르면_검색어가_삭제된다() {
        onView(withId(R.id.et_search_text_input))
            .perform(typeText("test"), closeSoftKeyboard())

        onView(withId(R.id.iv_delete_text_input))
            .perform(click())

        onView(withId(R.id.et_search_text_input))
            .check(matches(withText("")))
    }

    @Test
    fun 검색어를_입력하면_검색결과가_표시된다() {
        onView(withId(R.id.et_search_text_input))
            .perform(typeText("test"), closeSoftKeyboard())

        // bad
        Thread.sleep(1000)

        onView(withId(R.id.search_result_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun 검색어를_입력하지_않으면_검색어를_입력해_주세요_문구가_표시된다() {
        onView(withId(R.id.et_search_text_input))
            .perform(clearText(), closeSoftKeyboard())

        onView(withId(R.id.tv_empty))
            .check(matches(isDisplayed()))
    }
}
