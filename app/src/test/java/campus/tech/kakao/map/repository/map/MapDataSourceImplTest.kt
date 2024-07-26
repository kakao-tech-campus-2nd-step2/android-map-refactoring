package campus.tech.kakao.map.repository.map

import android.content.SharedPreferences
import campus.tech.kakao.map.repository.map.datasource.KEY_LAST_LATITUDE
import campus.tech.kakao.map.repository.map.datasource.KEY_LAST_LONGITUDE
import campus.tech.kakao.map.repository.map.datasource.MapDataSourceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MapDataSourceImplTest {

    private lateinit var preferences: SharedPreferences
    private lateinit var mapDataSourceImpl: MapDataSourceImpl

    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        mapDataSourceImpl = MapDataSourceImpl(preferences)
    }

    @Test
    fun `마지막 검색 대상 위치 정보를 저정한다`() {
        val latitude = 37.0
        val longitude = 127.0

        mapDataSourceImpl.updateLastPosition(latitude, longitude)

        verify { preferences.edit().putString(KEY_LAST_LATITUDE, latitude.toString()).apply() }
        verify { preferences.edit().putString(KEY_LAST_LONGITUDE, longitude.toString()).apply() }
    }

    @Test
    fun `저장된 마지막 검색 대상 위치 정보를 불러온다`() {
        val latitude = 37.0
        val longitude = 127.0

        every { preferences.getString(KEY_LAST_LATITUDE, null) } returns latitude.toString()
        every { preferences.getString(KEY_LAST_LONGITUDE, null) } returns longitude.toString()

        val result = mapDataSourceImpl.readLastPosition()

        assertEquals(Pair(latitude, longitude), result)
    }
}
