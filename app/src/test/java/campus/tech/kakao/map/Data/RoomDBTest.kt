package campus.tech.kakao.map.Data

import androidx.test.runner.AndroidJUnit4
import campus.tech.kakao.map.Data.Datasource.Local.DB.RoomDB
import campus.tech.kakao.map.Data.Datasource.Local.Entity.FavoriteEntity
import campus.tech.kakao.map.Domain.VO.PlaceCategory
import campus.tech.kakao.map.MyApplication
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class, manifest = Config.NONE)
@HiltAndroidTest
class RoomDBTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    private val fakePlace = FavoriteEntity(1, "name", "address", PlaceCategory.CAFE,1.0, 1.0)

    @Inject
    lateinit var roomDB: RoomDB

    @Before
    fun init() = runBlocking {
        hiltRule.inject()
        withContext(Dispatchers.IO) {
            roomDB.favoriteDao().addFavorite(fakePlace)
        }
    }

    @Test
    fun `검색한 키워드로 장소를 불러온다`() = runBlocking {
        val resultCafe = withContext(Dispatchers.IO) { roomDB.placeDao().getSimilarPlacesByName("카페") }
        val resultRestaurant = withContext(Dispatchers.IO) { roomDB.placeDao().getSimilarPlacesByName("음식점") }
        val resultPharmacy = withContext(Dispatchers.IO) { roomDB.placeDao().getSimilarPlacesByName("약국") }

        Assert.assertTrue(resultCafe.all { it.category == PlaceCategory.CAFE })
        Assert.assertTrue(resultRestaurant.all { it.category == PlaceCategory.RESTAURANT })
        Assert.assertTrue(resultPharmacy.all { it.category == PlaceCategory.PHARMACY })
    }

    @Test
    fun `id값으로 즐겨찾기 가져오기`() = runBlocking {
        val favorite = withContext(Dispatchers.IO) { roomDB.favoriteDao().getFavoriteById(fakePlace.id) }
        Assert.assertEquals(fakePlace, favorite)
    }

    @Test
    fun `존재하지 않는 항목이나 삭제된 항목을 찾을 시 null 반환`() = runBlocking {
        withContext(Dispatchers.IO) { roomDB.favoriteDao().deleteFavorite(fakePlace.id) }

        val favorite1 = withContext(Dispatchers.IO) { roomDB.favoriteDao().getFavoriteById(fakePlace.id) }
        val favorite2 = withContext(Dispatchers.IO) { roomDB.favoriteDao().getFavoriteById(-1) }

        Assert.assertEquals(null, favorite1)
        Assert.assertEquals(null, favorite2)
    }

    @Test
    fun `즐겨찾기 추가`() = runBlocking {
        val before = withContext(Dispatchers.IO) { roomDB.favoriteDao().getCurrentFavorite() }
        withContext(Dispatchers.IO) { roomDB.favoriteDao().deleteFavorite(fakePlace.id) }
        val after = withContext(Dispatchers.IO) { roomDB.favoriteDao().getCurrentFavorite() }

        after.add(fakePlace)
        Assert.assertEquals(before, after)
    }
}
