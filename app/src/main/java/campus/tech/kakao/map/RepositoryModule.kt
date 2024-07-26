package campus.tech.kakao.map

import campus.tech.kakao.map.model.repository.DefaultLocationRepository
import campus.tech.kakao.map.model.repository.DefaultSavedLocationRepository
import campus.tech.kakao.map.model.repository.LocationRepository
import campus.tech.kakao.map.model.repository.SavedLocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDefaultLocationRepository(impl: DefaultLocationRepository) : LocationRepository

    @Binds
    @Singleton
    abstract fun bindDefaultSavedLocationRepository(impl: DefaultSavedLocationRepository) : SavedLocationRepository
}