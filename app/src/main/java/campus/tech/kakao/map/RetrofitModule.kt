package campus.tech.kakao.map

import campus.tech.kakao.map.url.RetrofitData
import campus.tech.kakao.map.url.RetrofitService
import campus.tech.kakao.map.url.UrlContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

	@Provides
	@Singleton
	fun provideRetrofitService(retrofit: Retrofit): RetrofitService {
		return retrofit.create(RetrofitService::class.java)
	}

	@Provides
	@Singleton
	fun provideRetrofit():Retrofit {
		return Retrofit.Builder()
			.baseUrl(UrlContract.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	@Provides
	@Singleton
	fun provideRetrofitData(retrofitService: RetrofitService):RetrofitData{
		return RetrofitData(retrofitService)
	}



}