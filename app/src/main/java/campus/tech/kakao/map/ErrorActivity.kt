package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        val retryButton: Button = findViewById(R.id.retry_button)
        retryButton.setOnClickListener {
            val intent = Intent(this, Map_Activity::class.java)
            startActivity(intent)
            finish()
        }
    }
}