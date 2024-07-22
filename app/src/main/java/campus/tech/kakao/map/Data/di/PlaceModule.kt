package campus.tech.kakao.map.Data.di

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import campus.tech.kakao.map.Data.Datasource.Local.Dao.FavoriteDao
import campus.tech.kakao.map.Data.Datasource.Local.Dao.FavoriteDaoImpl
import campus.tech.kakao.map.Data.Datasource.Local.Dao.PlaceDao
import campus.tech.kakao.map.Data.Datasource.Local.Dao.PlaceDaoImpl
import campus.tech.kakao.map.Data.Datasource.Local.SqliteDB
import campus.tech.kakao.map.Data.Datasource.Remote.HttpUrlConnect
import campus.tech.kakao.map.Data.Datasource.Remote.RemoteService
import campus.tech.kakao.map.Data.Datasource.Remote.RetrofitService
import campus.tech.kakao.map.Data.PlaceRepositoryImpl
import campus.tech.kakao.map.Domain.Model.PlaceContract
import campus.tech.kakao.map.Domain.PlaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
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
        placeDao: PlaceDao,
        favoriteDao: FavoriteDao,
        retrofitService: RetrofitService,
        httpUrlConnect: RemoteService) : PlaceRepository =
        PlaceRepositoryImpl(
            placeDao, favoriteDao, retrofitService, httpUrlConnect
        )

    @Provides
    @Singleton
    fun provideSqliteDB(@ApplicationContext context: Context) =
        SqliteDB(context,PlaceContract.DATABASE_NAME,null,1)

    @Provides
    @Singleton
    fun providePlaceDao(database: SqliteDB) : PlaceDao =
        PlaceDaoImpl(database.writableDatabase)

    @Provides
    @Singleton
    fun provideFavoriteDao(database: SqliteDB) : FavoriteDao =
        FavoriteDaoImpl(database.writableDatabase)

    @Provides
    @Singleton
    fun provideRetrofitService() : RetrofitService = Retrofit.Builder()
        .baseUrl(RetrofitService.BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitService::class.java)

    @Provides
    @Singleton
    fun provideHttpUrlConnect() : RemoteService = HttpUrlConnect()

}