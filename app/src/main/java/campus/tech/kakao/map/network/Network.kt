package campus.tech.kakao.map.network

import campus.tech.kakao.map.BuildConfig
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class Network @Inject constructor(private val retrofitService: RetrofitService) {
    val API_KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"

    suspend fun searchCategory(categoryGroupCode: String): KakaoResponse? {
        val response = retrofitService.getSearchCategory(API_KEY, categoryGroupCode)
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception("응답 실패: ${response.errorBody()?.string()}")
        }
    }

    suspend fun searchKeyword(query: String): KakaoResponse? {
        val response = retrofitService.getSearchKeyword(API_KEY, query)
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception("응답 실패: ${response.errorBody()?.string()}")
        }
    }
}
