package campus.tech.kakao.map.viewmodel.keyword

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import campus.tech.kakao.map.model.kakaolocal.LocalUiModel
import campus.tech.kakao.map.repository.keyword.KeywordRepositoryImpl
import campus.tech.kakao.map.viewmodel.keyword.KeywordViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class KeywordViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var keywordRepositoryImpl: KeywordRepositoryImpl
    private lateinit var viewModel: KeywordViewModel

    @Before
    fun setUp() {
        keywordRepositoryImpl = mockk(relaxed = true)
        viewModel = KeywordViewModel(keywordRepositoryImpl)
    }

    @Test
    fun `키워드 히스토리를 가져온다`() {
        every { keywordRepositoryImpl.read() } returns listOf("카페", "약국")
        viewModel.readKeywordHistory()
        assertEquals(listOf("카페", "약국"), viewModel.keyword.value)
    }

    @Test
    fun `검색 결과 목록 중 카페를 선택했을 때 키워드 히스토리가 업데이트 된다`() {
        // given
        val keyword = "카페"
        every { keywordRepositoryImpl.read() } returns listOf(keyword)
        val localUiModel = mockk<LocalUiModel> {
            every { place } returns keyword
        }

        // when
        viewModel.onSearchItemClick(localUiModel)

        // then
        verify { keywordRepositoryImpl.delete(keyword) }
        verify { keywordRepositoryImpl.update(keyword) }
        assertEquals(listOf(keyword), viewModel.keyword.value)
    }

    @Test
    fun `키워드 히스토리 목록 중 카페를 제거했을 때 해당 키워드가 제거된다`() {
        // given
        val keyword = "약국"
        every { keywordRepositoryImpl.read() } returns listOf(keyword)

        // when
        viewModel.onKeywordItemDeleteClick(keyword)

        verify { keywordRepositoryImpl.delete(keyword) }
    }

    @Test
    fun `키워드 히스토리 목록 중 카페를 선택했을 때 키워드 히스토리가 업데이트 된다`() {
        // given
        val keyword = "카페"
        every { keywordRepositoryImpl.read() } returns listOf(keyword)

        // when
        viewModel.onKeywordItemClick(keyword)

        // then
        assertEquals(keyword, viewModel.keywordClicked.value)
        verify { keywordRepositoryImpl.delete(keyword) }
        verify { keywordRepositoryImpl.update(keyword) }
        assertEquals(listOf(keyword), viewModel.keyword.value)
    }
}
