package campus.tech.kakao.map.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R

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