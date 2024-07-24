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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityErrorBinding = DataBindingUtil.setContentView(this, R.layout.activity_error)
        binding.lifecycleOwner = this
        binding.error = errorViewModel

        val errorMessage = intent.getStringExtra("errorMessage")
        errorViewModel.setErrorMessage(errorMessage?:"Unknown")

    }
}