package campus.tech.kakao.map.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/* exportSchema 속성을 false로 설정하면, Room은 스키마 정보를 파일로 내보내지 않음
entities 속성은 이 데이터베이스에서 사용할 엔티티 클래스 목록을 지정 */
@Database(entities = [PlaceEntity::class], version = 1, exportSchema = false)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}




/*
    private val storeDao: StoreDao = database.storeDao()
    fun insertSampleData() {
        storeDao.insert(StoreEntity(name = "약국1", location = "서울시 성동구1"))
        storeDao.insert(StoreEntity(name = "약국2", location = "서울시 성동구2"))
이런 식으로 쓰임
 */