package ksc.campus.tech.kakao.map.models.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ksc.campus.tech.kakao.map.models.repositoriesImpl.SearchKeywordRepositoryImpl
import javax.inject.Singleton

interface SearchKeywordRepository {
    val keywords: LiveData<List<String>>
    suspend fun addKeyword(keyword: String)
    suspend fun deleteKeyword(keyword: String)
    suspend fun getKeywords()
}
@Module
@InstallIn(SingletonComponent::class)
object SearchKeywordRepositoryModule{
    @Provides
    @Singleton
    fun provideSearchKeywordRepository(
        @ApplicationContext context: Context
    ) : SearchKeywordRepository{
        return SearchKeywordRepositoryImpl(context)
    }
}