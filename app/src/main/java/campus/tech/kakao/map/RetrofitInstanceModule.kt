package campus.tech.kakao.map

import android.os.Looper
import android.widget.Toast
import campus.tech.kakao.map.model.datasource.KakaoAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstanceModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()

        // 가로채서 헤더에 키 추가하기
        val interceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "KakaoAK " + BuildConfig.KAKAO_REST_API_KEY)
                    .build()
                val response = chain.proceed(request)

                if (response.code != 200) {
                    android.os.Handler(Looper.getMainLooper()).post {
                        Toast.makeText(App(), "Error: ${response.code} - ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                return response
            }
        }
        return client.addInterceptor(interceptor).build()
    }
    @Provides
    @Singleton
    fun provideKakaoAPI(retrofit: Retrofit): KakaoAPI {
        return retrofit.create(KakaoAPI::class.java)
    }
}