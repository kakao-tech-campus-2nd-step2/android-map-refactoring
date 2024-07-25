package campus.tech.kakao.map.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityErrorBinding

class ErrorActivity : AppCompatActivity() {
    val errorViewModel: ErrorViewModel by viewModels()
    lateinit var errorBinding: ActivityErrorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        errorBinding = DataBindingUtil.setContentView(this, R.layout.activity_error)
        errorBinding.lifecycleOwner = this
        errorBinding.error = errorViewModel

        val errorMessage = intent.getStringExtra("errorMessage")
        errorViewModel.setErrorMessage(errorMessage?:"Unknown")

    }
}