package id.depok.posyandu.ui.auth

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import id.depok.posyandu.R
import id.depok.posyandu.base.BaseActivity
import id.depok.posyandu.data.preferences.ProfileCache
import id.depok.posyandu.ui.home.MainActivity
import id.depok.posyandu.utils.extension.launchActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.scope.launch {
            delay(2000)
            if (ProfileCache(this@SplashActivity).getSavedProfile() != null) {
                launchActivity<MainActivity> { }
            } else {
                launchActivity<LoginActivity> { }
            }
            finish()
        }
    }
}