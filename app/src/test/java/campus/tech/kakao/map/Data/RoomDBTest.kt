package campus.tech.kakao.map.Data

import androidx.test.runner.AndroidJUnit4
import campus.tech.kakao.map.Data.Datasource.Local.DB.RoomDB
import campus.tech.kakao.map.Data.Datasource.Local.Entity.FavoriteEntity
import campus.tech.kakao.map.Domain.VO.PlaceCategory
import campus.tech.kakao.map.MyApplication
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@Config(application = MyApplication::class, manifest = Config.NONE)
@HiltAndroidTest
class RoomDBTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    private val fakePlace = FavoriteEntity(1, "name", "address", PlaceCategory.CAFE,1.0, 1.0)

    @Inject
    lateinit var roomDB : RoomDB

    @Before
    fun init() {
        hiltRule.inject()
        roomDB.favoriteDao().addFavorite(
            FavoriteEntity(1, "name", "address", PlaceCategory.CAFE,1.0, 1.0)
        )
    }

    @Test
    fun `검색한 키워드로 장소를 불러온다`(){
        val resultCafe = roomDB.placeDao().getSimilarPlacesByName("카페")
        val resultRestaurant = roomDB.placeDao().getSimilarPlacesByName("음식점")
        val resultPharmacy = roomDB.placeDao().getSimilarPlacesByName("약국")

        Assert.assertTrue(resultCafe.all { it.category == PlaceCategory.CAFE })
        Assert.assertTrue(resultRestaurant.all { it.category == PlaceCategory.RESTAURANT })
        Assert.assertTrue(resultPharmacy.all { it.category == PlaceCategory.PHARMACY })
    }

    @Test
    fun `id값으로 즐겨찾기 가져오기`() {
        roomDB.favoriteDao().getFavoriteById(fakePlace.id).let{
            Assert.assertEquals(fakePlace, it)
        }
    }

    @Test
    fun `존재하지 않는 항목이나 삭제된 항목을 찾을 시 null 반환`(){
        roomDB.favoriteDao().deleteFavorite(fakePlace.id)

        Assert.assertEquals(null, roomDB.favoriteDao().getFavoriteById(fakePlace.id))
        Assert.assertEquals(null, roomDB.favoriteDao().getFavoriteById(-1))
    }

    @Test
    fun `즐겨찾기 추가`(){
        val before = roomDB.favoriteDao().getCurrentFavorite()
        roomDB.favoriteDao().deleteFavorite(fakePlace.id)
        val after = roomDB.favoriteDao().getCurrentFavorite()

        after.add(fakePlace)
        Assert.assertEquals(before, after)
    }
}