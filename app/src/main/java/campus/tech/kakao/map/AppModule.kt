package campus.tech.kakao.map

import android.content.Context
import android.content.SharedPreferences
import campus.tech.kakao.map.model.search.SearchKeywordDao
import campus.tech.kakao.map.repository.kakaomap.LastPositionRepository
import campus.tech.kakao.map.repository.search.KakaoSearchKeywordAPI
import campus.tech.kakao.map.repository.search.SavedSearchKeywordRepository
import campus.tech.kakao.map.repository.search.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSavedSearchKeywordRepository(searchKeywordDao: SearchKeywordDao) =
        SavedSearchKeywordRepository(searchKeywordDao)

    @Provides
    @Singleton
    fun provideSearchRepository(retrofitKakaoSearchKeyword: KakaoSearchKeywordAPI): SearchRepository =
        SearchRepository(retrofitKakaoSearchKeyword)

    @Provides
    @Singleton
    fun provideLastPositionRepository(sharedPreferences: SharedPreferences) =
        LastPositionRepository(sharedPreferences)
}