package campus.tech.kakao.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import campus.tech.kakao.map.viewModel.MapErrorViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MapErrorViewModelTest {
    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MapErrorViewModel

    @Mock
    private lateinit var errorMessageObserver: Observer<String>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MapErrorViewModel()
        viewModel.errorMessage.observeForever(errorMessageObserver)
    }

    @Test
    fun 오류메시지가_정상적으로_설정됨() = runTest {
        val error = "java.lang.Exception: 401"
        viewModel.setErrorMsg(error)
        Mockito.verify(errorMessageObserver).onChanged("401")
    }
}