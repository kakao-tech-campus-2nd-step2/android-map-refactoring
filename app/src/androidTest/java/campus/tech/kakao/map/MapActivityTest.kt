package campus.tech.kakao.map

import android.content.SharedPreferences
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import campus.tech.kakao.map.View.Map_Activity
import com.google.android.gms.maps.model.LatLng
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapActivityTest {

    private lateinit var scenario: ActivityScenario<Map_Activity>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setup() {
        editor = sharedPreferences.edit()

        scenario = ActivityScenario.launch(Map_Activity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun mapSuccess() {
        scenario.onActivity { activity ->
            val googleMap = scenario
            assertNotNull(googleMap)
        }
    }

    @Test
    fun lastLocationRequest() {
        val testLatLng = LatLng(37.5665, 126.9780)

        scenario.onActivity { activity ->
            val method = Map_Activity::class.java.getDeclaredMethod("saveLastLocation", Double::class.java, Double::class.java)
            method.isAccessible = true
            method.invoke(activity, testLatLng.latitude, testLatLng.longitude)

            val lastLatitude = sharedPreferences.getFloat("lastLatitude", 0f)
            val lastLongitude = sharedPreferences.getFloat("lastLongitude", 0f)

            assertEquals(testLatLng.latitude.toFloat(), lastLatitude)
            assertEquals(testLatLng.longitude.toFloat(), lastLongitude)

            val field = Map_Activity::class.java.getDeclaredField("lastKnownLocation")
            field.isAccessible = true
            val lastKnownLocation = field.get(activity) as LatLng

            assertEquals(testLatLng, lastKnownLocation)
        }
    }

    @Test
    fun showError401() {
        scenario.onActivity { activity ->
            activity.onMapError(401)
            onView(withId(R.id.error_message)).check(matches(isDisplayed()))
            onView(withId(R.id.error_message)).check(matches(withText("401 Unauthorized Error")))
        }
    }
}






