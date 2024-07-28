package campus.tech.kakao.map

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.model.database.AppDatabase
import campus.tech.kakao.map.model.database.DatabaseManager
import campus.tech.kakao.map.model.database.SavedSearchDao
import campus.tech.kakao.map.model.network.KakaoLocalService
import campus.tech.kakao.map.model.network.RetrofitInstance
import campus.tech.kakao.map.model.repository.MyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Singleton
//    @Provides
//    fun provideRepository(@ApplicationContext context: Context) : MyRepository{
//        return MyRepository(context)
//    }

//    @Singleton
//    @Provides
//    fun provideRepository() : MyRepository{
//        return MyRepository(savedSearchDao, apiService, sharedPreferences, editor)
//    }

    @Singleton
    @Provides
    fun provideDataBaseManager(@ApplicationContext context: Context) : DatabaseManager{
        return DatabaseManager(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideSavedSearchDao(database: AppDatabase): SavedSearchDao {
        return database.savedSearchDao()
    }

    @Provides
    fun provideKakaoLocalService(): KakaoLocalService {
        return RetrofitInstance.api
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("PlacePreferences", AppCompatActivity.MODE_PRIVATE)
    }

    @Provides
    fun provideSharedPreferencesEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

}