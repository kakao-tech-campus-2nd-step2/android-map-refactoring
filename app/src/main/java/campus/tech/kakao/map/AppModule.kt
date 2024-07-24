package campus.tech.kakao.map

import android.content.Context
import campus.tech.kakao.map.repository.kakaomap.LastPositionRepository
import campus.tech.kakao.map.repository.search.SavedSearchKeywordRepository
import campus.tech.kakao.map.repository.search.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideSavedSearchKeywordRepository(@ApplicationContext context: Context): SavedSearchKeywordRepository {
        return SavedSearchKeywordRepository(context)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(): SearchRepository {
        return SearchRepository()
    }

    @Provides
    @Singleton
    fun provideLastPositionRepository(@ApplicationContext context: Context): LastPositionRepository {
        return LastPositionRepository(context)
    }
}