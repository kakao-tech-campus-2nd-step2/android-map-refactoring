package campus.tech.kakao.map.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.model.repository.MyRepository

class MyViewModelFactory(
    private val context: Context,
    private val repository: MyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) { //isAssignableFrom->modelClass가 MyViewModel 클래스인지 확인
            @Suppress("UNCHECKED_CAST")
            return MyViewModel(context, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
