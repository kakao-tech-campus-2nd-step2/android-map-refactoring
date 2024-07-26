package campus.tech.kakao.map.presenter.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.base.ErrorEnum
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityErrorBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable


@AndroidEntryPoint
class ErrorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityErrorBinding
    private lateinit var type : ErrorEnum
    private lateinit var msg : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_error)

        getExtras()
        settingText()
        setRetryListener()
    }

    private fun getExtras(){
        type = intent.intentSerializable(ERROR_TYPE,ErrorEnum :: class.java) ?: ErrorEnum.ELSE
        msg = intent.extras?.getString(ERROR_MSG) ?: ERROR_UNKNOWN
    }

    private fun settingText(){
        binding.errorText.text = when(type){
            ErrorEnum.MAP_LOAD_ERROR -> getString(R.string.map_error)
            else -> getString(R.string.else_error)
        }

        binding.errorMsg.text = msg
    }

    private fun setRetryListener(){
        binding.retryBtn.setOnClickListener{
            val intent = when(type){
                ErrorEnum.MAP_LOAD_ERROR -> Intent(this,MapActivity::class.java)
                else -> null
            }

            intent?.let{
                startActivity(it)
            }
        }
    }

    private fun <T: Serializable> Intent.intentSerializable(key: String, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(key, clazz)
        } else {
            this.getSerializableExtra(key) as T?
        }
    }

    companion object{
        const val ERROR_TYPE = "type"
        const val ERROR_MSG = "msg"
        const val ERROR_UNKNOWN = "unknown"
    }
}

