package campus.tech.kakao.map.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import campus.tech.kakao.map.model.RetrofitInstance
import campus.tech.kakao.map.model.RetrofitService
import campus.tech.kakao.map.viewmodel.PlaceViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofitService(): RetrofitService {
        // RetrofitInstance.api 호출 -> RetrofitService 생성
        Log.d("testt", "Providing RetrofitService")
        return RetrofitInstance.api
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        Log.d("testt", "Providing SharedPreferences")
        // Module로 빼냄
        return context.getSharedPreferences("PlacePreferences", Context.MODE_PRIVATE)
    }

}
