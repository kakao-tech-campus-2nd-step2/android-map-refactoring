package campus.tech.kakao.map.di

import android.content.Context
import campus.tech.kakao.map.utility.SaveHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SaveModule {

    @Provides
    @Singleton
    fun provideSearchSaveHelper(@ApplicationContext context: Context): SaveHelper {
        return SaveHelper(context)
    }
}
