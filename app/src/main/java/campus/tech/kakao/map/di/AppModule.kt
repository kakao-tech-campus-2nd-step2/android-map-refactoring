package campus.tech.kakao.map.di

import android.app.Application
import androidx.room.Room
import campus.tech.kakao.map.network.AuthInterceptor
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.network.KakaoAPIRetrofitService
import campus.tech.kakao.map.model.AppDatabase
import campus.tech.kakao.map.model.PlaceDao
import campus.tech.kakao.map.repository.PlaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "place_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePlaceDao(database: AppDatabase): PlaceDao {
        return database.placeDao()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(BuildConfig.KAKAO_REST_API_KEY))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideKakaoApiService(retrofit: Retrofit): KakaoAPIRetrofitService {
        return retrofit.create(KakaoAPIRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun providePlaceRepository(apiService: KakaoAPIRetrofitService, placeDao: PlaceDao): PlaceRepository {
        return PlaceRepository(apiService, placeDao)
    }
}