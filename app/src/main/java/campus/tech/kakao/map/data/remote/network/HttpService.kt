package campus.tech.kakao.map.data.remote.network

import android.util.Log
import campus.tech.kakao.map.data.remote.entity.SearchResponse
import campus.tech.kakao.map.utils.ApiKeyProvider
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class HttpService @Inject constructor(
    private val apiKeyProvider: ApiKeyProvider,
    private val baseUrl: String
) {

    suspend fun searchKeyword(query: String): SearchResponse? = withContext(Dispatchers.IO) {
            val url = "${baseUrl}v2/local/search/keyword.json?query=$query"
            val connection = URL(url).openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", apiKeyProvider.KAKAO_REST_API_KEY)
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().readText()
                    Gson().fromJson(response, SearchResponse::class.java)
                } else {
                    Log.d(
                        "testt",
                        "Response failed: ${connection.errorStream.bufferedReader().readText()}")
                        null
                    null
                }
            } finally {
                connection.disconnect()
            }
        }
    }
