package campus.tech.kakao.map.data

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("locationInfo", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    @Named("latitude")
    fun provideLatitude(sharedPreferences: SharedPreferences): String {
        return sharedPreferences.getString("latitude", null) ?: "35.234"
    }

    @Provides
    @Singleton
    @Named("longitude")
    fun provideLongitude(sharedPreferences: SharedPreferences): String {
        return sharedPreferences.getString("longitude", null) ?: "129.0807"
    }
}