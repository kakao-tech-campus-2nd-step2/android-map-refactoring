package campus.tech.kakao.map.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R

class MapErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_error)
        val tvError : TextView = findViewById(R.id.tv_error)
        val refresh : ImageView = findViewById(R.id.refresh_button)

        tvError.text = intent.extras?.getString("error")

        refresh.setOnClickListener {
            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            finish()
        }

    }
}