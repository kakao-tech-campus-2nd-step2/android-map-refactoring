package campus.tech.kakao.map.data.net


import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.model.ResultSearchKeyword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KakaoApiClient {
    private const val BASE_URL = "https://dapi.kakao.com/"

    val api: KakaoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoApi::class.java)
    }

    suspend fun getPlaces(keyword: String): List<Place> =
        withContext(Dispatchers.IO) {
            val resultPlaces = mutableListOf<Place>()
            for (page in 1..3) {
                val response = api.getSearchKeyword(
                    key = BuildConfig.KAKAO_REST_API_KEY,
                    query = keyword,
                    size = 15,
                    page = page
                )
                if (response.isSuccessful) {
                    response.body()?.documents?.let { resultPlaces.addAll(it) }
                } else throw RuntimeException("통신 에러 발생")
            }
            resultPlaces
        }
}