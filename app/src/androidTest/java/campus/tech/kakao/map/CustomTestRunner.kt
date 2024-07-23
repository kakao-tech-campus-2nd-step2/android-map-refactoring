package campus.tech.kakao.map

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.test.runner.AndroidJUnitRunner
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.testing.HiltTestApplication

// A custom runner to set up the instrumented application class for tests.
// https://developer.android.com/training/dependency-injection/hilt-testing?hl=ko#instrumented-tests
class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        context?.let {
            KakaoMapSdk.init(context, getString(context, R.string.kakao_api_key))
        }
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}