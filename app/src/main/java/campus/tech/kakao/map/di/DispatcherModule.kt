package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.CoroutineIoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
internal object DispatchersModule {

    @Provides
    @CoroutineIoDispatcher
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}