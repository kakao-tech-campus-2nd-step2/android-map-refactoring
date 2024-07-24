package ksc.campus.tech.kakao.map.models.repositories

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import ksc.campus.tech.kakao.map.models.repositoriesImpl.SearchResultRepositoryImpl
import javax.inject.Singleton

@Serializable
data class SearchResult(
    val id: String,
    val name: String,
    val address: String,
    val type: String,
    val latitude: Double,
    val longitude: Double
)

interface SearchResultRepository {
    val searchResult: Flow<List<SearchResult>>
    fun search(text: String, apiKey: String)
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