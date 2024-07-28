package campus.tech.kakao.map.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.databinding.ActivityMapErrorBinding

class MapErrorActivity : AppCompatActivity() {
    private lateinit var errorMessage : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_error)

        val binding : ActivityMapErrorBinding = DataBindingUtil.setContentView(this, R.layout.activity_map_error)
        binding.error = this

        errorMessage = intent.extras?.getString("error") ?: ""

        binding.errorText.text = errorMessage
    }
}