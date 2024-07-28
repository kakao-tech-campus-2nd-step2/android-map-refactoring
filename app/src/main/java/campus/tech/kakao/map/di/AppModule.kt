package campus.tech.kakao.map.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
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
        return RetrofitInstance.api
    }

    @Singleton
    @Provides
    // 기본 공유 환경 설정 생성
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    // retrofitService, sharedPreferences 인자 -> PlaceViewModel 생성
    fun providePlaceViewModel(
        retrofitService: RetrofitService,
        sharedPreferences: SharedPreferences
    ): PlaceViewModel {
        return PlaceViewModel(retrofitService, sharedPreferences)
    }
}
