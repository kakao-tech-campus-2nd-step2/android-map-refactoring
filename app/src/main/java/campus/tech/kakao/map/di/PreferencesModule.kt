package campus.tech.kakao.map.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    @PreferencesName
    fun providePreferencesName(): String {
        return "search_prefs"
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context, @PreferencesName prefsName: String): SharedPreferences {
        return appContext.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }
}
