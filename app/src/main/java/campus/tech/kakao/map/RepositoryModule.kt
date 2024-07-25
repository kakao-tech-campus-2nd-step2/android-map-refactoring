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
// ViewModelComponent로 설정해도 될까요? Repository가 ViewModel에서 쓰이니까 ViewModelComponent를 쓰면 되겠다고 생각했는데요..
// 적절한 Component를 선택하는 기준이 뭔지 모르겠습니다!
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDefaultLocationRepository(impl: DefaultLocationRepository) : LocationRepository

    @Binds
    @Singleton
    abstract fun bindDefaultSavedLocationRepository(impl: DefaultSavedLocationRepository) : SavedLocationRepository
}