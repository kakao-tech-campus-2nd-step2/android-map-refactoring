package campus.tech.kakao.map.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseName

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PreferencesName

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiBaseUrl
