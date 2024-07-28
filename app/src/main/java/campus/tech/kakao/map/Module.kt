package campus.tech.kakao.map

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.dto.MapPositionPreferences
import campus.tech.kakao.map.dto.SearchWordContract
import campus.tech.kakao.map.dto.SearchWordDao
import campus.tech.kakao.map.dto.SearchWordDatabase
import campus.tech.kakao.map.url.RetrofitData
import campus.tech.kakao.map.url.RetrofitService
import campus.tech.kakao.map.url.UrlContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

	@Provides
	@Singleton
	fun provideRetrofitService(): RetrofitService {
		return Retrofit.Builder()
			.baseUrl(UrlContract.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(RetrofitService::class.java)
	}

	@Provides
	@Singleton
	fun provideRetrofitData(retrofitService: RetrofitService):RetrofitData{
		return RetrofitData(retrofitService)
	}

	@Provides
	@Singleton
	fun provideDatabase(@ApplicationContext context: Context): SearchWordDatabase{
		return Room.databaseBuilder(context, SearchWordDatabase::class.java, SearchWordContract.DB_NAME)
			.build()
	}

	@Provides
	@Singleton
	fun provideDao(database: SearchWordDatabase): SearchWordDao {
		return database.searchWordDao()
	}

	@Provides
	@Singleton
	fun provideMapPosition(@ApplicationContext context: Context): MapPositionPreferences {
		return MapPositionPreferences(context)
	}

}