package campus.tech.kakao.map.repository.location

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import campus.tech.kakao.map.model.kakaolocal.Document
import campus.tech.kakao.map.network.KakaoLocalService
import campus.tech.kakao.map.model.kakaolocal.KakaoLocalResponse
import campus.tech.kakao.map.model.kakaolocal.LocalUiModel
import campus.tech.kakao.map.model.kakaolocal.Meta
import campus.tech.kakao.map.model.kakaolocal.SameName
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoLocationImplTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var mockService: KakaoLocalService
    private lateinit var mockCall: Call<KakaoLocalResponse>
    private lateinit var kakaoLocationImpl: KakaoLocationImpl

    @Before
    fun setUp() {
        mockService = mockk()
        mockCall = mockk(relaxed = true)
        kakaoLocationImpl = KakaoLocationImpl(mockService)
    }

    @Test
    fun `카페 검색을 요청한다`() {
        // given
        val keyword = "카페"
        every { mockService.searchKeyword(query = keyword) } returns mockCall

        // when
        kakaoLocationImpl.search(keyword)

        // then
        verify { mockService.searchKeyword(query = keyword) }
        verify { mockCall.enqueue(any()) }
    }

    @Test
    fun `카페 검색 후 응답 값을 받는다`() {
        // given
        val keyword = "카페"
        every { mockService.searchKeyword(query = keyword) } returns mockCall

        // when
        kakaoLocationImpl.search(keyword)

        // then
        val callbackSlot = slot<Callback<KakaoLocalResponse>>()
        verify { mockCall.enqueue(capture(callbackSlot)) }

        // Simulate a successful response
        val kakaoLocalResponse = KakaoLocalResponse(
            listOf(
                Document(
                    "addressName",
                    "categoryGroupCode",
                    "categoryGroupName",
                    "categoryName",
                    "distance",
                    "id",
                    "String",
                    "placeName",
                    "placeUrl",
                    "roadAddressName",
                    "0.0",
                    "0.0"
                )
            ),
            Meta(false, 0, SameName("", listOf(""), ""), 0)
        )
        val mockResponse = Response.success(kakaoLocalResponse)
        callbackSlot.captured.onResponse(mockCall, mockResponse)

        assertEquals(kakaoLocationImpl.items.value, mockResponse.body()?.toUiModel())
    }

    private fun KakaoLocalResponse.toUiModel(): List<LocalUiModel> {
        return documents.map {
            LocalUiModel(
                it.placeName,
                it.addressName,
                it.categoryGroupName,
                it.y.toDouble(),
                it.x.toDouble()
            )
        }
    }
}
