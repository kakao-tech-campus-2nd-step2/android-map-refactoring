package campus.tech.kakao.map.data

import androidx.test.runner.AndroidJUnit4
import campus.tech.kakao.map.domain.VO.PlaceCategory
import campus.tech.kakao.map.domain.PlaceRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class, manifest = Config.NONE)
@HiltAndroidTest
class PlaceRepositoryTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this@PlaceRepositoryTest)

    @Inject
    lateinit var placeRepository: PlaceRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `카카오 API 활용해서 키워드 검색(Retrofit)`() {
        CoroutineScope(Dispatchers.IO).launch {
            val resultCafe = placeRepository.searchPlaceRemote("카페")
            val resultPharmacy = placeRepository.searchPlaceRemote("약국")

            assertTrue(resultCafe.all { it.category == PlaceCategory.CAFE })
            assertTrue(resultPharmacy.all { it.category == PlaceCategory.PHARMACY })
        }
    }

    @Test
    fun `카카오 API 활용해서 키워드 검색(HttpUrlConnection)`() {
        val resultCafe = placeRepository.getPlaceByNameHTTP("카페")
        val resultPharmacy = placeRepository.getPlaceByNameHTTP("약국")

        assertTrue(resultCafe.all { it.category == PlaceCategory.CAFE })
        assertTrue(resultPharmacy.all { it.category == PlaceCategory.PHARMACY })
    }

    @Test
    fun `검색 중 가능할 시 15개 이상의 데이터를 보여준다(Retrofit)`() {
        CoroutineScope(Dispatchers.IO).launch {
            assertTrue(placeRepository.searchPlaceRemote("카페").size > 15)
        }
    }

    @Test
    fun `검색 중 가능할 시 15개 이상의 데이터를 보여준다(HttpUrlConnection)`() {
        assertTrue(placeRepository.getPlaceByNameHTTP("카페").size > 15)
    }


}