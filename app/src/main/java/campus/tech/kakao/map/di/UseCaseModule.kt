package campus.tech.kakao.map.di

import campus.tech.kakao.map.domain.repository.LocationRepository
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.repository.SavedSearchWordRepository
import campus.tech.kakao.map.domain.usecase.DeleteSearchWordByIdUseCase
import campus.tech.kakao.map.domain.usecase.GetAllSearchWordsUseCase
import campus.tech.kakao.map.domain.usecase.GetPlacesByCategoryUseCase
import campus.tech.kakao.map.domain.usecase.InsertOrUpdateSearchWordUseCase
import campus.tech.kakao.map.domain.usecase.LoadLocationUseCase
import campus.tech.kakao.map.domain.usecase.SaveLocationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetPlacesByCategoryUseCase(
        placeRepository: PlaceRepository,
    ): GetPlacesByCategoryUseCase {
        return GetPlacesByCategoryUseCase(placeRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideInsertOrUpdateSearchWordUseCase(
        savedSearchWordRepository: SavedSearchWordRepository,
    ): InsertOrUpdateSearchWordUseCase {
        return InsertOrUpdateSearchWordUseCase(savedSearchWordRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteSearchWordByIdUseCase(
        savedSearchWordRepository: SavedSearchWordRepository,
    ): DeleteSearchWordByIdUseCase {
        return DeleteSearchWordByIdUseCase(savedSearchWordRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllSearchWordsUseCase(
        savedSearchWordRepository: SavedSearchWordRepository,
    ): GetAllSearchWordsUseCase {
        return GetAllSearchWordsUseCase(savedSearchWordRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSaveLocationUseCase(
        locationRepository: LocationRepository,
    ): SaveLocationUseCase {
        return SaveLocationUseCase(locationRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideLoadLocationUseCase(
        locationRepository: LocationRepository,
    ): LoadLocationUseCase {
        return LoadLocationUseCase(locationRepository)
    }
}
