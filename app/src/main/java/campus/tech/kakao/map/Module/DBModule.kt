package campus.tech.kakao.map.Module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import campus.tech.kakao.map.Room.MapDatabase
import campus.tech.kakao.map.Room.SelectMapItemDao
import campus.tech.kakao.map.kakaoAPI.NetworkService
import campus.tech.kakao.map.kakaoAPI.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DBModule {
    @Singleton
    @Provides
    fun provideMapDatabase(
        @ApplicationContext context: Context
    ): MapDatabase = Room.databaseBuilder(
        context,
        MapDatabase::class.java, "kakaoMapDB"
    ).build()

    @Singleton
    @Provides
    fun provideSelectMapItemDao(mapDatabase: MapDatabase): SelectMapItemDao =
        mapDatabase.selectMapItemDao()

    @Singleton
    @Provides
    fun provideRetrofitService(): RetrofitService = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitService::class.java)

    @Singleton
    @Provides
    fun provideNetworkService(retrofitService : RetrofitService): NetworkService = NetworkService(retrofitService)

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences("lastPos", Context.MODE_PRIVATE)
}