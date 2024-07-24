package campus.tech.kakao.map.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import campus.tech.kakao.map.data.AppDatabase
import campus.tech.kakao.map.data.KeywordDao
import campus.tech.kakao.map.data.KakaoLocalApiService
import campus.tech.kakao.map.repository.Repository
import campus.tech.kakao.map.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "SearchData.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideKeywordDao(db: AppDatabase): KeywordDao {
        return db.keywordDao()
    }

    @Provides
    @Singleton
    fun provideKakaoLocalApiService(): KakaoLocalApiService {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoLocalApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("search_prefs", Context.MODE_PRIVATE)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        repositoryImpl: RepositoryImpl
    ): Repository
}
