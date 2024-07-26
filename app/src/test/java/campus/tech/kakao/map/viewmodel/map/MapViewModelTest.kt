package campus.tech.kakao.map.viewmodel.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import campus.tech.kakao.map.model.state.UiState
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import campus.tech.kakao.map.repository.map.MapRepository
import campus.tech.kakao.map.viewmodel.map.MapViewModel
import com.kakao.vectormap.MapAuthException
import io.mockk.every
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Rule

class MapViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var mapRepository: MapRepository
    private lateinit var viewModel: MapViewModel
    private val latitude = 0.1
    private val longitude = 0.2

    @Before
    fun setUp() {
        mapRepository = mockk(relaxed = true)
        viewModel = MapViewModel(mapRepository)
    }

    @Test
    fun `지도 인증 실패 시 뷰 상태가 Error이다`() {
        val exception = MapAuthException("")
        viewModel.showErrorView(exception)

        assertEquals(UiState.Error(exception), viewModel.uiState.value)
    }

    @Test
    fun `지도 인증 성공 시 뷰 상태가 Success이다`() {
        viewModel.showSuccessView()

        assertEquals(UiState.Success, viewModel.uiState.value)
    }

    @Test
    fun `마지막 위치 정보를 가져온다`() {
        every { mapRepository.readLastPosition() } returns Pair(latitude, longitude)

        viewModel.readLastPosition()
        verify { mapRepository.readLastPosition() }
        assertEquals(Pair(latitude, longitude), viewModel.readLastPosition())
    }
}
