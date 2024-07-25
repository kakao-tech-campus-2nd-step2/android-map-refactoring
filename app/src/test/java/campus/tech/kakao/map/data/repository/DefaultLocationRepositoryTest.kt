package campus.tech.kakao.map.data.repository

import androidx.datastore.core.DataStore
import campus.tech.kakao.map.domain.model.LocationDomain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultLocationRepositoryTest {
    private lateinit var dataStore: DataStore<LocationDomain>
    private lateinit var locationRepository: DefaultLocationRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dataStore = mockk()
        locationRepository = DefaultLocationRepository(dataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSaveLocation() = runTest(testDispatcher) {
        // given
        val location = LocationDomain("Test Place", 123.456, 78.90, "Test Address")
        coEvery { dataStore.updateData(any()) } coAnswers {
            val updateFunction = arg<suspend (LocationDomain) -> LocationDomain>(0)
            updateFunction(location)
            location
        }

        // when
        locationRepository.saveLocation(location)

        // then
        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun testLoadLocation() = runTest {
        val location = LocationDomain("Test Place", 123.456, 78.90, "Test Address")
        val locationFlow = MutableStateFlow(location)
        coEvery { dataStore.data } returns locationFlow

        // when
        val result = locationRepository.loadLocation()

        // then
        assertEquals(location, result)
    }

    @Test
    fun `loadLocation 함수는 저장된 데이터가 없을 때 기본값을 반환한다`() = runTest(testDispatcher) {
        // given
        coEvery { dataStore.data } returns flowOf()

        // When
        val result = locationRepository.loadLocation()

        // Then
        assertEquals("부산대 컴공관", result.name)
        assertEquals(35.230934, result.latitude, 0.0)
        assertEquals(129.082476, result.longitude, 0.0)
        assertEquals("부산광역시 금정구 부산대학로 63번길 2", result.address)
    }
}
