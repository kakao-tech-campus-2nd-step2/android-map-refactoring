package campus.tech.kakao.map.utility

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {
    @Provides
    @Singleton
    fun singletonMapUtiliy(): MapUtility{
        return MapUtility
    }
}