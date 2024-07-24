package campus.tech.kakao.map.network

import campus.tech.kakao.map.BuildConfig
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class Network @Inject constructor(private val retrofitService: RetrofitService) {
    val API_KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"

    fun searchCategory(categoryGroupCode: String, callback: Callback<KakaoResponse>) {
        retrofitService.getSearchCategory(API_KEY, categoryGroupCode).enqueue(callback)
    }

    fun searchKeyword(query: String, callback: Callback<KakaoResponse>) {
        retrofitService.getSearchKeyword(API_KEY, query).enqueue(callback)
    }
}
