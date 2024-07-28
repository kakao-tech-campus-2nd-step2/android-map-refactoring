package campus.tech.kakao.map

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import campus.tech.kakao.map.presentation.MapActivity
import campus.tech.kakao.map.presentation.SearchActivity
import campus.tech.kakao.map.presentation.adapter.SearchedPlaceAdapter
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var context: Context

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(SearchActivity::class.java)

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        sharedPreferences = context.getSharedPreferences("mockk", Context.MODE_PRIVATE)
        Intents.init()
    }

    @After
    fun after() {
        sharedPreferences.edit().clear().apply()
        Intents.release()
    }

    @Test
    fun testSearchAndVerifyMapActivityLaunched() {
        onView(withId(R.id.edtSearch)).perform(click()).perform(replaceText("부산대"))

        Thread.sleep(1200L)
        onView(withId(R.id.recyclerPlace))
            .perform(
                RecyclerViewActions.actionOnHolderItem(
                    instanceOf(SearchedPlaceAdapter.LocationViewHolder::class.java), click()
                ).atPosition(3)
            )

        Intents.intended(hasComponent(MapActivity::class.java.name))
    }
}
