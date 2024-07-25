package campus.tech.kakao.map

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.view.View
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class PlaceApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        initKakaoMapSdk()
    }

    private fun initKakaoMapSdk(){
        val key = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, key)
    }
    companion object {
        @Volatile
        private lateinit var appInstance: PlaceApplication
        fun isNetworkActive(): Boolean {
            val connectivityManager: ConnectivityManager =
                appInstance.getSystemService(ConnectivityManager::class.java)
            val network: Network = connectivityManager.activeNetwork ?: return false
            val actNetwork: NetworkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
}
