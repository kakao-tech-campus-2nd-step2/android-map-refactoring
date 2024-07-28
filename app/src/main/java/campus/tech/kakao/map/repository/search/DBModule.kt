package campus.tech.kakao.map.repository.search

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {
    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            SearchKeywordDB::class.java,
            "searchKeyword"
        ).build()

    @Provides
    fun provideDao(db: SearchKeywordDB) =
        db.searchKeywordDao()
}