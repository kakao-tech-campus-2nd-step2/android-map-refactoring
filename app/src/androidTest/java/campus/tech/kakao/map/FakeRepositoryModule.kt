package campus.tech.kakao.map

import campus.tech.kakao.map.data.history.History
import campus.tech.kakao.map.data.local_search.Location
import campus.tech.kakao.map.di.RepositoryModule
import campus.tech.kakao.map.domain.repository.HistoryRepository
import campus.tech.kakao.map.domain.repository.LastLocationRepository
import campus.tech.kakao.map.domain.repository.SearchLocationRepository
import com.kakao.vectormap.LatLng
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object FakeRepositoryModule {
    @Provides
    @Singleton
    fun provideHistoryRepository(): HistoryRepository =
        mockk<HistoryRepository>().also {
            coEvery { it.getHistory() } returns listOf(
                History(name = "기록1"), History(name = "기록2")
            )
            coEvery { it.addHistory(any()) } just Runs
            coEvery { it.removeHistory(any()) } just Runs
        }

    @Provides
    @Singleton
    fun provideLastLocationRepository(): LastLocationRepository =
        mockk<LastLocationRepository>().also {
            coEvery { it.saveLocation(any(), any()) } just Runs
            every { it.loadLocation() } returns flowOf(LatLng.from(37.1, 127.1))
        }

    @Provides
    @Singleton
    fun provideSearchLocationRepository(): SearchLocationRepository =
        mockk<SearchLocationRepository>().also {
            val testLocation = listOf(
                Location("카페1", "주소1", "카페", 37.1, 127.1),
                Location("카페2", "주소2", "카페", 37.2, 127.2)
            )
            coEvery { it.searchLocation(any()) } returns testLocation
        }
}