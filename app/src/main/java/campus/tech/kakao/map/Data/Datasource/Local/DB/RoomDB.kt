package campus.tech.kakao.map.Data.Datasource.Local.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import campus.tech.kakao.map.Data.Datasource.Local.Dao.FavoriteDao
import campus.tech.kakao.map.Data.Datasource.Local.Entity.FavoriteEntity
import campus.tech.kakao.map.Data.Datasource.Local.Dao.PlaceDao
import campus.tech.kakao.map.Data.Datasource.Local.Entity.PlaceEntity

@Database(entities = [PlaceEntity::class, FavoriteEntity::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun placeDao() : PlaceDao
    abstract fun favoriteDao() : FavoriteDao
}