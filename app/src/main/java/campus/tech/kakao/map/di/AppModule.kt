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
        return RetrofitInstance.api
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    fun providePlaceViewModel(
        retrofitService: RetrofitService,
        sharedPreferences: SharedPreferences
    ): PlaceViewModel {
        return PlaceViewModel(retrofitService, sharedPreferences)
    }
}
