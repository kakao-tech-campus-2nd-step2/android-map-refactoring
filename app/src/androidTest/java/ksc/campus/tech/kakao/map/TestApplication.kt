package ksc.campus.tech.kakao.map

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(Application::class)
class TestApplication: Application() {
}