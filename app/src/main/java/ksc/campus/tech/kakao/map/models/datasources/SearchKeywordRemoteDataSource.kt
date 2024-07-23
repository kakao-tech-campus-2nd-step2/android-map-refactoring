package ksc.campus.tech.kakao.map.models.datasources

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ksc.campus.tech.kakao.map.models.dao.SearchKeywordDao
import ksc.campus.tech.kakao.map.models.entities.SearchKeyword
import javax.inject.Inject

@Database(entities = [SearchKeyword::class], version = 4)
abstract class SearchKeywordDB : RoomDatabase() {
    abstract fun dao(): SearchKeywordDao
}

class SearchKeywordRemoteDataSource @Inject constructor(
    private val roomDB: SearchKeywordDB
) {
    fun insertOrReplaceKeyword(keyword: String) {
        val data = SearchKeyword(0, keyword)
        roomDB.dao().insert(data)
    }

    fun deleteKeyword(keyword: String) {
        roomDB.dao().deleteWhere(keyword)
    }

    fun queryAllSearchKeywords(): Flow<List<String>> {
        val keywords = roomDB.dao().queryAllKeywords()
        return keywords.map { keyword ->
            keyword.map {
                it.keyword ?:""
            }
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SearchKeywordDBModule {
    @Provides
    fun provideSearchKeywordDB(
        @ApplicationContext context: Context
    ): SearchKeywordDB {
        return Room.databaseBuilder(context, SearchKeywordDB::class.java, "MapSearch2")
            .fallbackToDestructiveMigration().build()
    }
}