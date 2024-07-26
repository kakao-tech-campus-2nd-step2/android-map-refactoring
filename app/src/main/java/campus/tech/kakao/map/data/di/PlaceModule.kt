package campus.tech.kakao.map.data.di

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.data.datasource.Local.DB.RoomDB
import campus.tech.kakao.map.data.datasource.Remote.HttpUrlConnect
import campus.tech.kakao.map.data.datasource.Remote.RemoteService
import campus.tech.kakao.map.data.datasource.Remote.RetrofitService
import campus.tech.kakao.map.data.PlaceRepositoryImpl
import campus.tech.kakao.map.domain.PlaceRepository
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
object PlaceModule {
    @Provides
    @Singleton
    fun providePlaceRepository(
        roomDB: RoomDB,
        retrofitService: RetrofitService,
        remoteService: RemoteService) : PlaceRepository =
        PlaceRepositoryImpl(
            roomDB, retrofitService, remoteService
        )

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RoomDB::class.java,"db").build()

    @Provides
    @Singleton
    fun provideRetrofitService() : RetrofitService = Retrofit.Builder()
        .baseUrl(RetrofitService.BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitService::class.java)

    @Provides
    @Singleton
    fun provideRemoteService() : RemoteService = HttpUrlConnect()

}