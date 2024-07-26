package campus.tech.kakao.map.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

class SearchService @Inject constructor(private val network: Network, private val context: Context) {

    suspend fun searchKeyword(query: String): KakaoResponse? {
        return try {
            network.searchKeyword(query)
        } catch (error: Throwable) {
            Log.e("SearchService", "요청 실패", error)
            throw error
        }
    }

    suspend fun searchCategory(categoryGroupCode: String): KakaoResponse? {
        return try {
            network.searchCategory(categoryGroupCode)
        } catch (error: Throwable) {
            Log.e("SearchService", "요청 실패", error)
            throw error
        }
    }
}