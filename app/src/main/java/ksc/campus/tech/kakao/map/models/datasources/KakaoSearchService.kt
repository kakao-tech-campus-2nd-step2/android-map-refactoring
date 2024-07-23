package ksc.campus.tech.kakao.map.models.datasources

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ksc.campus.tech.kakao.map.BuildConfig
import ksc.campus.tech.kakao.map.models.dto.KeywordSearchResponse
import ksc.campus.tech.kakao.map.models.repositories.SearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject

interface KakaoSearchRetrofitService {
    @GET("/v2/local/search/keyword.json")
    fun requestSearchResultByKeyword(
        @Header("Authorization") restApiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Call<KeywordSearchResponse>
}

class KakaoSearchService @Inject constructor(
    private val retrofitService: KakaoSearchRetrofitService
) {
    /**
     * 요청이 유효한지 검증하기 위해 사용.
     *
     * 현재 진행중인 쿼리가 유저가 마지막으로 보낸 쿼리인지 검증한다.
     *
     * 유저가 새로운 쿼리를 보낼 때마다 [lastSearchId] 값이 1씩 증가한다.
     */
    private var lastSearchId: Int = 0

    private fun isQueryValid(query: String): Boolean = query.isNotBlank()

    private fun parseCategory(category: String) =
        category.split('>').last().trim().replace(",", ", ")

    private fun responseToResultArray(response: Response<KeywordSearchResponse>): List<SearchResult> {
        val result = mutableListOf<SearchResult>()

        for (doc in response.body()?.documents ?: listOf()) {
            result.add(
                SearchResult(
                    doc.id,
                    doc.placeName,
                    doc.addressName,
                    CATEGORY_CODE_DESCRIPTION_MAP.getOrDefault(
                        doc.categoryGroupCode,
                        parseCategory(doc.categoryName)
                    ),
                    doc.y.toDoubleOrNull() ?: 0.0,
                    doc.x.toDoubleOrNull() ?: 0.0
                )
            )
        }
        return result
    }

    private fun isResponseSuccess(response: Response<KeywordSearchResponse>): Boolean {
        if (!response.isSuccessful) {
            Log.e("KSC", "request failed")
            Log.e("KSC", "error message: ${response.message()}")
            Log.e("KSC", "error code: ${response.code()}")
            return false
        }
        return true
    }

    fun batchSearchByKeyword(
        query: String,
        apiKey: String,
        batchCount: Int,
        onResponse: ((results: List<SearchResult>) -> Unit)?
    ) {
        lastSearchId++

        batchSearchByKeyword(lastSearchId, query, apiKey, 1, batchCount, onResponse)
    }


    private fun batchSearchByKeyword(
        searchId: Int,
        query: String,
        apiKey: String,
        page: Int,
        batchCount: Int,
        onResponse: ((results: List<SearchResult>) -> Unit)?
    ) {
        if (page > batchCount)
            return

        if (!isQueryValid(query))
            return

        retrofitService.requestSearchResultByKeyword("KakaoAK $apiKey", query, page).enqueue(
            object : Callback<KeywordSearchResponse> {
                override fun onResponse(
                    call: Call<KeywordSearchResponse>,
                    response: Response<KeywordSearchResponse>
                ) {
                    if (!isResponseSuccess(response)) {
                        return
                    }
                    val result = responseToResultArray(response)
                    if (lastSearchId != searchId) {
                        return
                    }
                    onResponse?.invoke(result)
                    if (response.body()?.meta?.isEnd == false) {
                        batchSearchByKeyword(
                            searchId,
                            query,
                            apiKey,
                            page + 1,
                            batchCount,
                            onResponse
                        )
                    }
                }

                override fun onFailure(call: Call<KeywordSearchResponse>, p1: Throwable) {
                    if (call.isCanceled) {
                        Log.e("KSC", "request canceled")
                    }
                    if (call.isExecuted) {
                        Log.e("KSC", "request was executed but failed")
                    }
                    Log.e("KSC", "Message: ${p1.message}")
                }
            }
        )
    }

    companion object {
        private val CATEGORY_CODE_DESCRIPTION_MAP: HashMap<String, String> = hashMapOf(
            Pair("MT1", "대형마트"),
            Pair("CS2", "편의점"),
            Pair("PS3", "어린이집, 유치원"),
            Pair("SC4", "학교"),
            Pair("AC5", "학원"),
            Pair("PK6", "주차장"),
            Pair("OL7", "주유소, 충전소"),
            Pair("SW8", "지하철역"),
            Pair("BK9", "은행"),
            Pair("CT1", "문화시설"),
            Pair("AG2", "중개업소"),
            Pair("PO3", "공공기관"),
            Pair("AT4", "관광명소"),
            Pair("AD5", "숙박"),
            Pair("FD6", "음식점"),
            Pair("CE7", "카페"),
            Pair("HP8", "병원"),
            Pair("PM9", "약국")
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SearchKakaoRetrofitService {
    @Provides
    fun provideSearchKakaoRetrofitService(
        @ApplicationContext context: Context
    ): KakaoSearchRetrofitService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoSearchRetrofitService::class.java)
    }
}