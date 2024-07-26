package campus.tech.kakao.map.di

import android.content.Context
import campus.tech.kakao.map.model.RetrofitService
import campus.tech.kakao.map.model.local.Repository
import campus.tech.kakao.map.repository.local.PlaceDao
import campus.tech.kakao.map.repository.local.PlaceDatabase
import campus.tech.kakao.map.repository.local.RepositoryImpl
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
object Module {

    private val BASE_URL = "https://dapi.kakao.com/"

    @Provides
    @Singleton
    fun provideRetrofit(): RetrofitService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }


}


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PlaceDatabase {
        return DatabaseProvider.getDatabase(context)
    }

    @Provides
    fun providePlaceDao(database: PlaceDatabase): PlaceDao {
        return database.placeDao()
    }

    @Provides
    @Singleton
    fun provideRepository(placeDao: PlaceDao): Repository {
        return RepositoryImpl(placeDao)
    }
}
