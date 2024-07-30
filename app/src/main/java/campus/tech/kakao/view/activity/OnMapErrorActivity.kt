package campus.tech.kakao.view.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.OnmaperrorActivityBinding

class OnMapErrorActivity : AppCompatActivity() {
    private lateinit var binding: OnmaperrorActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.onmaperror_activity)
        binding.activity = this
        showError()
    }

    private fun showError() {
        val typeError: String? = intent.getStringExtra("ErrorType")
        binding.errortype.text = typeError ?: "Unknown error"
    }

    fun whenClickRefreshButton() {
        binding.refreshButton.visibility = GONE
        binding.contentLoading.visibility = VISIBLE
    }
}