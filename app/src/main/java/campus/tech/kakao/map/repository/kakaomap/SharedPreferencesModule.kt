package campus.tech.kakao.map.repository.kakaomap

import android.content.Context
import campus.tech.kakao.map.view.ActivityKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences(
            ActivityKeys.PREFS,
            Context.MODE_PRIVATE
        )
}