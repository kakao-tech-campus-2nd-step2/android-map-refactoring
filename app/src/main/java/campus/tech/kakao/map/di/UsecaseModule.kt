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
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class UsecaseModule {

    @Binds
    abstract fun bindSaveLastPlaceUseCase(
        impl: SaveLastPlaceUseCaseImpl
    ): SaveLastPlaceUseCase


    @Binds
    abstract fun bindGetLastPlaceUseCase(
        impl: GetLastPlaceUseCaseImpl
    ): GetLastPlaceUseCase

    @Binds
    abstract fun bindGetSearchPlacesUseCase(
        impl: GetSearchPlacesUseCaseImpl
    ): GetSearchPlacesUseCase

    @Binds
    abstract fun bindSaveSearchQueryUseCase(
        impl: SaveSearchQueryUseCaseImpl
    ): SaveSearchQueryUseCase

    @Binds
    abstract fun bindGetSearchHistoryUseCase(
        impl: GetSearchHistoryUseCaseImpl
    ): GetSearchHistoryUseCase

    @Binds
    abstract fun bindRemoveSearchQueryUseCase(
        impl: RemoveSearchQueryUseCaseImpl
    ): RemoveSearchQueryUseCase
}
