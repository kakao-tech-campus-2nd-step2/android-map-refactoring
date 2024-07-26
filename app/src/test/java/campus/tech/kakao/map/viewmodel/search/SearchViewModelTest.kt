package campus.tech.kakao.map.viewmodel.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import campus.tech.kakao.map.model.kakaolocal.LocalUiModel
import campus.tech.kakao.map.repository.location.KakaoLocationImpl
import campus.tech.kakao.map.viewmodel.search.SearchViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var locationSearcher: KakaoLocationImpl
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        locationSearcher = mockk(relaxed = true)
        viewModel = SearchViewModel(locationSearcher)
    }

    @Test
    fun `카페를 검색하면 검색 결과로 카페가 반환된다`() {
        // given
        val keyword = "카페"

        // when
        viewModel.searchLocationData(keyword)

        // then
        verify { locationSearcher.search(keyword) }
    }

    @Test
    fun `검색 키워드가 공백이면 검색 결과가 빈 리스트로 반환된다`() {
        // given
        val keyword = ""

        // when
        viewModel.searchLocationData(keyword)

        // then
        assertEquals(listOf<LocalUiModel>(), viewModel.items.value)
    }

    @Test
    fun `검색 결과 중 카페를 선택하면 해당 카페 정보가 지도 정보에 전달된다`() {
        // given
        val localUiModel = LocalUiModel("카페", "", "", 0.0, 0.0)

        // when
        viewModel.onSearchItemClick(localUiModel)

        // then
        assertEquals(localUiModel, viewModel.localInformation.value)
    }
}
