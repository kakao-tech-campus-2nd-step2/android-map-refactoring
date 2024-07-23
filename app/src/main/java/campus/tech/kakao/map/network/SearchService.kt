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

    fun searchKeyword(query: String, callback: (KakaoResponse?) -> Unit, errorCallback: (Throwable) -> Unit) {
        network.searchKeyword(query, object : Callback<KakaoResponse> {
            override fun onResponse(call: Call<KakaoResponse>, response: Response<KakaoResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    errorCallback(Exception("응답 실패"))
                    Log.e("SearchService", "응답 실패")
                }
            }

            override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                errorCallback(t)
                Log.e("SearchService", "요청 실패", t)
            }
        })
    }

    fun searchCategory(categoryGroupCode: String, callback: (KakaoResponse?) -> Unit, errorCallback: (Throwable) -> Unit) {
        network.searchCategory(categoryGroupCode, object : Callback<KakaoResponse> {
            override fun onResponse(call: Call<KakaoResponse>, response: Response<KakaoResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    errorCallback(Exception("응답 실패"))
                    Log.e("SearchService", "응답 실패")
                }
            }

            override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                errorCallback(t)
                Log.e("SearchService", "요청 실패", t)
            }
        })
    }
}
