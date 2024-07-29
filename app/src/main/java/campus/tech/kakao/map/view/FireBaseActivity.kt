package campus.tech.kakao.map.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class FireBaseActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermission()
        
        
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // 개발용
        }
        remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            fetchValues()
        }
    }


    private fun FirebaseRemoteConfig.fetchValues() {
        fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val state = getString("serviceState")
                Log.d("james", "state: $state")

            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // 권한 요청 이유를 설명하는 UI를 표시
                showNotificationPermissionDialog()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // 버전 다른
        }
    }

    private fun showNotificationPermissionDialog() {
//        AlertDialog.Builder(this@MainActivity).apply {
//            setTitle("권한 요청 재확인")
//            setMessage(
//                String.format(
//                    "다양한 알림 소식을 받기 위해 권한을 허용하시겠어요?\n(알림 에서 %s의 알림 권한을 허용해주세요.)",
//                    getString(R.string.app_name)
//                )
//            )
//            setPositiveButton("YES") { _, _ ->
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                val uri = Uri.fromParts("package", packageName, null)
//                intent.data = uri
//                startActivity(intent)
//            }
//            setNegativeButton("NO") { _, _ -> }
//            show()
//        }
        }
}