package campus.tech.kakao.map

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.runner.AndroidJUnitRunner
import campus.tech.kakao.map.Data.Datasource.Local.DB.RoomDB
import campus.tech.kakao.map.Data.Datasource.Remote.HttpUrlConnect
import campus.tech.kakao.map.Data.Datasource.Remote.RemoteService
import campus.tech.kakao.map.Data.Datasource.Remote.RetrofitService
import campus.tech.kakao.map.Data.PlaceRepositoryImpl
import campus.tech.kakao.map.Data.di.PlaceModule
import campus.tech.kakao.map.Domain.PlaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PlaceModule::class]
)
object PlaceTestModule {
    @Provides
    @Singleton
    fun providePlaceRepository(
        roomDB: RoomDB,
        retrofitService: RetrofitService,
        remoteService: RemoteService
    ) : PlaceRepository =
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