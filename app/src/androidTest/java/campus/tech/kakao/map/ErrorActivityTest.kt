package campus.tech.kakao.map

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import campus.tech.kakao.map.View.ErrorActivity
import campus.tech.kakao.map.View.Map_Activity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ErrorActivityTest {

    private lateinit var scenario: ActivityScenario<ErrorActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(ErrorActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testErrorActivityIsDisplayed() {
        onView(withId(R.id.error_message)).check(matches(isDisplayed()))
        onView(withId(R.id.retry_button)).check(matches(isDisplayed()))
    }

    @Test
    fun testRetryButton() {
        onView(withId(R.id.retry_button)).perform(click())

        val intendedIntent = Intent(getInstrumentation().targetContext, Map_Activity::class.java)
        intendedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        assertEquals(intendedIntent.component, (getInstrumentation().context as AppCompatActivity))
    }
}
