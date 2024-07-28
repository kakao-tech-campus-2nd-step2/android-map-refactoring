package campus.tech.kakao.map

import android.content.Context
import campus.tech.kakao.map.model.database.DatabaseManager
import campus.tech.kakao.map.model.repository.MyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRepository(@ApplicationContext context: Context) : MyRepository{
        return MyRepository(context)
    }

    @Singleton
    @Provides
    fun provideDataBaseManager(@ApplicationContext context: Context) : DatabaseManager{
        return DatabaseManager(context)
    }

}