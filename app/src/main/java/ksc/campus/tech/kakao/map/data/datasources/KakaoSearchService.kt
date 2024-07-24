package ksc.campus.tech.kakao.map.data.datasources

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ksc.campus.tech.kakao.map.BuildConfig
import ksc.campus.tech.kakao.map.data.entities.Document
import ksc.campus.tech.kakao.map.data.entities.KeywordSearchResponse
import retrofit2.Call
import retrofit2.HttpException
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

    private fun isQueryValid(query: String): Boolean = query.isNotBlank()

    private fun responseToArray(response: Response<KeywordSearchResponse>): List<Document> = response.body()?.documents?: listOf()

    private fun isResponseSuccess(response: Response<KeywordSearchResponse>): Boolean {
        if (!response.isSuccessful) {
            Log.e("KSC", "request failed")
            Log.e("KSC", "error message: ${response.message()}")
            Log.e("KSC", "error code: ${response.code()}")
            return false
        }
        return true
    }

    fun searchResult(
        query: String,
        apiKey: String,
        batchCount: Int): Flow<List<Document>>{
        return flow {
            try {
                val result = if (query == "") listOf() else batchSearchByKeyword(
                    query,
                    apiKey,
                    1,
                    batchCount
                )
                Log.d("KSC", "Searched")
                emit(result ?: listOf())
            } catch (e: HttpException) {
                Log.e("KSC", e.message ?: "")
                emit(listOf())
            } catch (e: Exception) {
                Log.e("KSC", e.message ?: "")
                emit(listOf())
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun batchSearchByKeyword(
        query: String,
        apiKey: String,
        page: Int,
        batchCount: Int
    ): List<Document> {
        if (page > batchCount)
            return listOf()

        if (!isQueryValid(query))
            return listOf()

        val result = mutableListOf<Document>()
        val response =
            retrofitService.requestSearchResultByKeyword("KakaoAK $apiKey", query, page).execute()

        if (!isResponseSuccess(response)) {
            Log.e("KSC", "Message: ${response.message()}")
            return listOf()
        }
        result += responseToArray(response)
        if (response.body()?.meta?.isEnd == false) {
            result += batchSearchByKeyword(
                query,
                apiKey,
                page + 1,
                batchCount
            )
        }

        return result
    }

    companion object {
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