package campus.tech.kakao.map

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.dto.MapPositionPreferences
import campus.tech.kakao.map.dto.SearchWord
import campus.tech.kakao.map.dto.SearchWordDao
import campus.tech.kakao.map.url.RetrofitData
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
@HiltAndroidTest
class FunTest {

	@get:Rule
	var hiltRule = HiltAndroidRule(this)

	private lateinit var model: MainViewModel
	private lateinit var viewModelStore: ViewModelStore
	private lateinit var context: Context
	@Inject
	lateinit var retrofitData: RetrofitData
	@Inject
	lateinit var databaseDao: SearchWordDao
	@Inject
	lateinit var mapPosition: MapPositionPreferences
	@Before
	fun setUp() {
		context = RuntimeEnvironment.getApplication()
		viewModelStore = ViewModelStore()
		hiltRule.inject()
		model = MainViewModel(context as Application, retrofitData, databaseDao, mapPosition)
	}
	@Test
	fun 검색어를_입력하면_검색_결과_표시(){
		val query = "이안아파트"
		val expectedQueryResultName = arrayOf("이안금곡아파트", "대우이안아파트", "이안금곡아파트 관리사무소",
			"대우이안아파트 정문", "이안동래센트럴시티아파트", "이안금곡아파트 입주자대표회의 전기차충전소",
			"대우이안아파트 상가동", "CU 화명대우이안점", "대우이안아파트 지하주차장",
			"이안공인중개사사무소", "부산시 북구 대우이안아파트 전기차충전소", "대우이안공인중개사사무소",
			"삼계이안아파트", "이안센트럴포레장유1단지아파트", "성일이안시티아파트")
		model.searchLocalAPI(query)
		val actualQueryResult = model.documentList.value
		actualQueryResult?.forEach { document ->
			assert(expectedQueryResultName.contains(document.placeName))
		}
	}

	@Test
	fun 검색어_저장_되고_삭제도_되는지_확인(){
		val query = Document(
			"이안아파트", "아파트",
			"남양주", "10",
			"10")
		val expectedResult = SearchWord(
			"이안아파트", "남양주", "아파트")
		model.addWord(query)
		val result = model.wordList.value?.contains(expectedResult)
		if (result != null) assert(result) else assert(false)

		val query2 = SearchWord(
			"이안아파트", "남양주", "아파트")
		model.deleteWord(query2)
		val result2 = model.wordList.value?.contains(query2)
		if (result2 != null) assertFalse(result2) else assert(false)
	}
}