package ksc.campus.tech.kakao.map.domain.repositories

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import ksc.campus.tech.kakao.map.data.repositoryimpls.SearchResultRepositoryImpl
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import javax.inject.Singleton


interface SearchResultRepository {
    fun search(text: String, apiKey: String): Flow<List<SearchResult>>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchResultRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSearchResultRepository(
        searchResultRepositoryImpl: SearchResultRepositoryImpl
    ): SearchResultRepository
}