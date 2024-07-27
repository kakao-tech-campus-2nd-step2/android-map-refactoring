package campus.tech.kakao.map.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapErrorBinding
import campus.tech.kakao.map.viewModel.MapErrorViewModel

class MapErrorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapErrorBinding
    private lateinit var viewModel: MapErrorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setBinding()
        setViewModel()
        setErrorMsg()
        clickBackBtn()
    }

    private fun setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_error)
        binding.activity = this
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[MapErrorViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setErrorMsg() {
        val errorMsg = intent.getStringExtra("ERROR_MESSAGE")
        viewModel.setErrorMsg(errorMsg)
    }

    private fun clickBackBtn() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }
}