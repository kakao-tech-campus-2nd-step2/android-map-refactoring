package campus.tech.kakao.map

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.map.Room.MapDatabase
import campus.tech.kakao.map.Room.MapItem
import campus.tech.kakao.map.Room.SelectMapItemDao
import campus.tech.kakao.map.database.KakaoMapItemDbHelper
import campus.tech.kakao.map.database.SelectItemDao
import campus.tech.kakao.map.kakaoAPI.NetworkService
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

//@HiltAndroidTest
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class SelectItemRoomTest {

//    @get:Rule
//    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var selectMapItemDao : SelectMapItemDao

//    @Before
//    fun init() {
//        hiltRule.inject()
//    }

    @Test
    fun checkInsertSelectItem() {
        selectMapItemDao.insertItem(MapItem(1, "name", "address", "cate", "x", "y", "1"))
        val isExist = selectMapItemDao.checkItemInDB("1")
        assertEquals(true, isExist)
    }

    @Test
    fun checkDeleteSelectItem() {
        selectMapItemDao.insertItem(MapItem(1, "name", "address", "cate", "x", "y", "1"))
        selectMapItemDao.deleteItem("1")
        val isExist = selectMapItemDao.checkItemInDB("1")
        assertEquals(false, isExist)
    }

    @Test
    fun checkmakeAllSelectItemList() {
        selectMapItemDao.insertItem(MapItem(1, "name", "address", "cate", "x", "y", "1"))
        selectMapItemDao.insertItem(MapItem(2, "name2", "address2", "cate2", "x", "y", "2"))
        selectMapItemDao.insertItem(MapItem(3, "name3", "address3", "cate3", "x", "y", "3"))
        val selectMapItems = selectMapItemDao.getAll()
        assertEquals(3, selectMapItems.size)
    }

//
//    @After
//    fun tearDown() {
//        mapItemDB.close()
//    }

}