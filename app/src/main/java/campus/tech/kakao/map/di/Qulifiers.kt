package campus.tech.kakao.map.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocationDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SearchWordDatabase

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher
