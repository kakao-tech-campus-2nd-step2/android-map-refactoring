package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.usecase.GetLastPlaceUseCaseImpl
import campus.tech.kakao.map.data.usecase.GetSearchHistoryUseCaseImpl
import campus.tech.kakao.map.data.usecase.GetSearchPlacesUseCaseImpl
import campus.tech.kakao.map.data.usecase.RemoveSearchQueryUseCaseImpl
import campus.tech.kakao.map.data.usecase.SaveLastPlaceUseCaseImpl
import campus.tech.kakao.map.data.usecase.SaveSearchQueryUseCaseImpl
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.usecase.GetLastPlaceUseCase
import campus.tech.kakao.map.domain.usecase.GetSearchHistoryUseCase
import campus.tech.kakao.map.domain.usecase.GetSearchPlacesUseCase
import campus.tech.kakao.map.domain.usecase.RemoveSearchQueryUseCase
import campus.tech.kakao.map.domain.usecase.SaveLastPlaceUseCase
import campus.tech.kakao.map.domain.usecase.SaveSearchQueryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UsecaseModule {

    @Provides
    @Singleton
    fun provideSaveLastPlaceUseCase(
        placeRepository: PlaceRepository
    ): SaveLastPlaceUseCase {
        return SaveLastPlaceUseCaseImpl(placeRepository)
    }

    @Provides
    @Singleton
    fun provideGetLastPlaceUseCase(
        placeRepository: PlaceRepository
    ): GetLastPlaceUseCase {
        return GetLastPlaceUseCaseImpl(placeRepository)
    }

    @Provides
    @Singleton
    fun provideGetSearchPlacesUseCase(
        placeRepository: PlaceRepository
    ): GetSearchPlacesUseCase {
        return GetSearchPlacesUseCaseImpl(placeRepository)
    }

    @Provides
    @Singleton
    fun provideSaveSearchQueryUseCase(
        placeRepository: PlaceRepository
    ): SaveSearchQueryUseCase {
        return SaveSearchQueryUseCaseImpl(placeRepository)
    }

    @Provides
    @Singleton
    fun provideGetSearchHistoryUseCase(
        placeRepository: PlaceRepository
    ): GetSearchHistoryUseCase {
        return GetSearchHistoryUseCaseImpl(placeRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveSearchQueryUseCase(
        placeRepository: PlaceRepository
    ): RemoveSearchQueryUseCase {
        return RemoveSearchQueryUseCaseImpl(placeRepository)
    }
}
