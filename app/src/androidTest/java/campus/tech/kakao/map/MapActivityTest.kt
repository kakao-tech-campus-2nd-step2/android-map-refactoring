package campus.tech.kakao.map

import android.content.SharedPreferences
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.runner.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class MapActivityTest {

    private lateinit var scenario: ActivityScenario<Map_Activity>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setup() {
        // Create a mock for SharedPreferences and its editor
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        `when`(sharedPreferences.edit()).thenReturn(editor)

        // Start the activity scenario
        scenario = ActivityScenario.launch(Map_Activity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun mapSuccess() {
        scenario.onActivity { activity ->
            // Check if the map is initialized
            assertNotNull(activity.googleMap)
        }
    }

    @Test
    private fun '앱 종료 시 마지막 위치를 저장하여 다시 앱 실행 시 해당 위치로 포커스'() {
        scenario.onActivity { activity ->
            val testLatLng = LatLng(37.5665, 126.9780) // Example coordinates for Seoul

            activity.saveLastLocation(testLatLng.latitude, testLatLng.longitude)

            verify(editor).putFloat("lastLatitude", testLatLng.latitude.toFloat())
            verify(editor).putFloat("lastLongitude", testLatLng.longitude.toFloat())
            verify(editor).apply()

            `when`(sharedPreferences.getFloat("lastLatitude", 0f)).thenReturn(testLatLng.latitude.toFloat())
            `when`(sharedPreferences.getFloat("lastLongitude", 0f)).thenReturn(testLatLng.longitude.toFloat())

            assertEquals(testLatLng, activity.lastKnownLocation)
        }
    }

    @Test
    fun `맵이 제대로 실행되지 않을 때 401에러 화면 보여주기`() {
        scenario.onActivity { activity ->
            // Simulate a 401 error by calling onMapError with error code 401
            activity.onMapError(401)

            // Check if the error message is displayed
            onView(withId(R.id.error_view)).check(matches(isDisplayed()))
            onView(withId(R.id.error_message)).check(matches(withText("401 Unauthorized Error")))
        }
    }
}




