package campus.tech.kakao.View.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import campus.tech.kakao.ViewModel.MainViewModel
import campus.tech.kakao.map.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel.checkNetworkAvailability(this)
        mainViewModel.isNetworkAvailable.observe(this, Observer { isAvailable ->
            if (!isAvailable) {
                Toast.makeText(this, "인터넷 연결 안됨", Toast.LENGTH_SHORT).show()
            }
        })

        if (savedInstanceState == null) {
            mainViewModel.setMapFragment()
        }

        mainViewModel.currentFragment.observe(this, Observer { fragment ->
            replaceFragment(fragment)
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun clearBackStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}