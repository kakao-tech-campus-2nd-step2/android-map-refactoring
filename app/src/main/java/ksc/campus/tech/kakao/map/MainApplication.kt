package ksc.campus.tech.kakao.map

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ksc.campus.tech.kakao.map.models.repositories.MapViewRepository
import ksc.campus.tech.kakao.map.models.repositories.SearchKeywordRepository
import ksc.campus.tech.kakao.map.models.repositories.SearchResultRepository
import ksc.campus.tech.kakao.map.models.repositoriesImpl.MapViewRepositoryImpl
import ksc.campus.tech.kakao.map.models.repositoriesImpl.SearchKeywordRepositoryImpl
import ksc.campus.tech.kakao.map.models.repositoriesImpl.SearchResultRepositoryImpl

@HiltAndroidApp
class MainApplication: Application() {
}