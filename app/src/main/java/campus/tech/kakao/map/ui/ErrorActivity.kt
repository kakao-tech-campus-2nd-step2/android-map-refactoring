package campus.tech.kakao.map.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R

class ErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        val errorMessage = intent.getStringExtra("errorMessage")
        val tvError = findViewById<TextView>(R.id.tvError)
        tvError.text = errorMessage

    }
}