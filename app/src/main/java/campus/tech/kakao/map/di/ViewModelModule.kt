package campus.tech.kakao.map.di

import android.content.Context
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.data.*
import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.data.net.KakaoApi
import campus.tech.kakao.map.domain.repository.PlaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    // SearchViewModel
    @Provides
    fun providePlaceRepository(
        placeDao: PlaceDao, kakaoApi: KakaoApi): PlaceRepository {
        return if (PlaceApplication.isNetworkActive()) {
            PlaceRemoteDataRepository(placeDao,kakaoApi)
        } else {
            PlaceLocalDataRepository(placeDao)
        }
    }

    // MapViewModel
    @Provides
    fun provideLastVisitedPlaceManager(@ApplicationContext context: Context): LastVisitedPlaceManager{
        return LastVisitedPlaceManager(context)
    }
}
