package campus.tech.kakao.map

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.model.AppDatabase
import campus.tech.kakao.map.model.KakaoLocalService
import campus.tech.kakao.map.repository.MapRepository
import campus.tech.kakao.map.repository.MapRepositoryImpl
import campus.tech.kakao.map.repository.SearchRepository
import campus.tech.kakao.map.repository.SearchRepositoryImpl
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
object NetworkModule {
    @Provides
    @Singleton
    @ApiKey
    fun provideApiKey(): String = "KakaoAK ${BuildConfig.KAKAO_API_KEY}"
    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl(): String = "https://dapi.kakao.com/"

    @Provides
    @Singleton
    fun provideKakaoLocalService(@BaseUrl baseUrl: String): KakaoLocalService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoLocalService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun MapRepository(
        mapRepositoryImpl: MapRepositoryImpl
    ): MapRepository

    @Binds
    @Singleton
    abstract fun SearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository
}


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    @DatabaseName
    fun provideDatabaseName(): String = "placedb"

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        @DatabaseName databaseName: String
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, databaseName
        ).build()
    }

    @Provides
    @Singleton
    fun provideSavePlaceDao(db: AppDatabase) = db.savePlaceDao()
}
