package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.repository.LocationRepository
import campus.tech.kakao.map.data.repository.PlaceRepository
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import campus.tech.kakao.map.domain.usecase.DeleteSearchWordByIdUseCase
import campus.tech.kakao.map.domain.usecase.GetAllSearchWordsUseCase
import campus.tech.kakao.map.domain.usecase.GetPlacesByCategoryUseCase
import campus.tech.kakao.map.domain.usecase.InsertOrUpdateSearchWordUseCase
import campus.tech.kakao.map.domain.usecase.LoadLocationUseCase
import campus.tech.kakao.map.domain.usecase.SaveLocationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetPlacesByCategoryUseCase(
        placeRepository: PlaceRepository,
    ): GetPlacesByCategoryUseCase {
        return GetPlacesByCategoryUseCase(placeRepository)
    }

    @Provides
    @Singleton
    fun provideInsertOrUpdateSearchWordUseCase(
        savedSearchWordRepository: SavedSearchWordRepository,
    ): InsertOrUpdateSearchWordUseCase {
        return InsertOrUpdateSearchWordUseCase(savedSearchWordRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteSearchWordByIdUseCase(
        savedSearchWordRepository: SavedSearchWordRepository,
    ): DeleteSearchWordByIdUseCase {
        return DeleteSearchWordByIdUseCase(savedSearchWordRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllSearchWordsUseCase(
        savedSearchWordRepository: SavedSearchWordRepository,
    ): GetAllSearchWordsUseCase {
        return GetAllSearchWordsUseCase(savedSearchWordRepository)
    }

    @Provides
    @Singleton
    fun provideSaveLocationUseCase(
        locationRepository: LocationRepository,
    ): SaveLocationUseCase {
        return SaveLocationUseCase(locationRepository)
    }

    @Provides
    @Singleton
    fun provideLoadLocationUseCase(
        locationRepository: LocationRepository,
    ): LoadLocationUseCase {
        return LoadLocationUseCase(locationRepository)
    }
}
