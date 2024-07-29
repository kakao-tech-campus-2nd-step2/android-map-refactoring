package campus.tech.kakao.map.model

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