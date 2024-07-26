package campus.tech.kakao.map.di

import campus.tech.kakao.map.domain.usecaseImpl.GetLastPlaceUseCaseImpl
import campus.tech.kakao.map.domain.usecaseImpl.GetSearchHistoryUseCaseImpl
import campus.tech.kakao.map.domain.usecaseImpl.GetSearchPlacesUseCaseImpl
import campus.tech.kakao.map.domain.usecaseImpl.RemoveSearchQueryUseCaseImpl
import campus.tech.kakao.map.domain.usecaseImpl.SaveLastPlaceUseCaseImpl
import campus.tech.kakao.map.domain.usecaseImpl.SaveSearchQueryUseCaseImpl
import campus.tech.kakao.map.domain.usecase.GetLastPlaceUseCase
import campus.tech.kakao.map.domain.usecase.GetSearchHistoryUseCase
import campus.tech.kakao.map.domain.usecase.GetSearchPlacesUseCase
import campus.tech.kakao.map.domain.usecase.RemoveSearchQueryUseCase
import campus.tech.kakao.map.domain.usecase.SaveLastPlaceUseCase
import campus.tech.kakao.map.domain.usecase.SaveSearchQueryUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
