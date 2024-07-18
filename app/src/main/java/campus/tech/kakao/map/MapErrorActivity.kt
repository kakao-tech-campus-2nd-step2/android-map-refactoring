package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MapErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_error)
        val tvError : TextView = findViewById(R.id.tv_error)
        val refresh : ImageView = findViewById(R.id.refresh_button)

        tvError.text = intent.extras?.getString("error")

        refresh.setOnClickListener {
            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
            val intent =Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}