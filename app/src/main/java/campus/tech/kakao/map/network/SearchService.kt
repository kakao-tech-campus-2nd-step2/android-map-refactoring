package campus.tech.kakao.map.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchService @Inject constructor(private val network: Network, private val context: Context) {

    fun searchKeyword(query: String, callback: (KakaoResponse?) -> Unit) {
        network.searchKeyword(query, object : Callback<KakaoResponse> {
            override fun onResponse(call: Call<KakaoResponse>, response: Response<KakaoResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    Toast.makeText(context, "응답 실패", Toast.LENGTH_SHORT).show()
                    Log.e("SearchService", "응답 실패")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                Toast.makeText(context, "요청 실패: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("SearchService", "요청 실패", t)
                callback(null)
            }
        })
    }

    fun searchCategory(categoryGroupCode: String, callback: (KakaoResponse?) -> Unit) {
        network.searchCategory(categoryGroupCode, object : Callback<KakaoResponse> {
            override fun onResponse(call: Call<KakaoResponse>, response: Response<KakaoResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    Toast.makeText(context, "응답 실패", Toast.LENGTH_SHORT).show()
                    Log.e("SearchService", "응답 실패")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                Toast.makeText(context, "요청 실패: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("SearchService", "요청 실패", t)
                callback(null)
            }
        })
    }
}
