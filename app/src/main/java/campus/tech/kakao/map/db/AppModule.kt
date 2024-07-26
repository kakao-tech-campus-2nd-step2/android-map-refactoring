package campus.tech.kakao.map.db

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.network.KakaoLocalApi
import campus.tech.kakao.map.repository.KakaoRepository
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
    fun provideDatabase(@ApplicationContext context: Context): DataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            DataBase::class.java,
            "kyle_maps_database"
        ).build()
    }

    @Provides
    fun provideSearchHistoryDao(database: DataBase): SearchHistoryDao {
        return database.searchHistoryDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoLocalApi(retrofit: Retrofit): KakaoLocalApi {
        return retrofit.create(KakaoLocalApi::class.java)
    }

    @Provides
    @Singleton
    fun provideKakaoRepository(
        retrofit: Retrofit,
    ): KakaoRepository {
        return KakaoRepository(retrofit)
    }
}
