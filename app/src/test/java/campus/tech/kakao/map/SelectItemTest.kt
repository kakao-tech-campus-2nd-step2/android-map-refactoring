package campus.tech.kakao.map

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.map.Room.MapDatabase
import campus.tech.kakao.map.Room.SelectMapItemDao
import campus.tech.kakao.map.database.KakaoMapItemDbHelper
import campus.tech.kakao.map.database.SelectItemDao
import campus.tech.kakao.map.kakaoAPI.NetworkService
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject


@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class SelectItemTest {
    private lateinit var mapItemDB : KakaoMapItemDbHelper
    private lateinit var selectItemDao : SelectItemDao

    @Inject
    lateinit var selectMapItemDao : SelectMapItemDao

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mapItemDB = KakaoMapItemDbHelper(context)
        val wDb = mapItemDB.writableDatabase
        val rDb = mapItemDB.readableDatabase
        selectItemDao = SelectItemDao(wDb, rDb)
    }

    @Test
    fun checkCreated() {
        val isExist = selectItemDao.checkTableExist()
        assertEquals(true, isExist)
    }

    @Test
    fun checkInsertSelectItem() {
        selectItemDao.insertSelectItem("1번", "1")
        val isExist = selectItemDao.checkItemInDB("1")
        assertEquals(true, isExist)
    }

    @Test
    fun checkDeleteSelectItem() {
        selectItemDao.insertSelectItem("1번", "1")
        selectItemDao.deleteSelectItem("1")
        val isExist = selectItemDao.checkItemInDB("1")
        assertEquals(false, isExist)
    }

    @Test
    fun checkmakeAllSelectItemList() {
        selectItemDao.insertSelectItem("1번", "1")
        selectItemDao.insertSelectItem("2번", "2")
        selectItemDao.insertSelectItem("3번", "3")
        val selectMapItems = selectItemDao.makeAllSelectItemList()
        assertEquals(3, selectMapItems.size)
    }

    @After
    fun tearDown() {
        mapItemDB.close()
    }

}