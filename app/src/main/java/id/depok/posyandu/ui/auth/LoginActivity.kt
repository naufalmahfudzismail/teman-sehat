package id.depok.posyandu.ui.auth

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import id.depok.posyandu.BuildConfig
import id.depok.posyandu.R
import id.depok.posyandu.base.BaseActivity
import id.depok.posyandu.data.preferences.ProfileCache
import id.depok.posyandu.ui.home.MainActivity
import id.depok.posyandu.utils.extension.launchActivity
import id.depok.posyandu.utils.extension.toast
import id.depok.posyandu.utils.helper.AuthHelper
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            val window = this.window
            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.statusBarColor =
                this.let { ContextCompat.getColor(it, R.color.colorWhite) }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                val decor = window.decorView
                decor.systemUiVisibility =
                    decor.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window?.statusBarColor =
                    this.let { ContextCompat.getColor(it, R.color.colorWhite) }

            }
        }

        "Version : ${BuildConfig.VERSION_NAME}".also { lblVersion.text = it }

        btnLogin.setOnClickListener {
            val userName = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (userName.isEmpty() || password.isEmpty()) {
                toast("Isi username dan password!")
            } else {
                viewModel.login(userName, password)
            }
        }

        viewModel.loginState.observe(this) { state ->
            state.data?.let {
                if (it.user != null) {
                    viewModel.getKader(it.user?.id.toString())
                } else {
                    toast("User tidak ditemukan")
                }
            }
            state.loading.let {
                if(it) {
                    showLoading("Loading...")
                } else hideLoading()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
        }

        viewModel.kaderState.observe(this) { state ->
            state.data?.let {
                if (it.kader != null) {
                    ProfileCache(this).saveProfile(it)
                    launchActivity<MainActivity> { }
                    finish()
                } else {
                    toast("User tidak ditemukan")
                }
            }
            state.loading.let {
                if(it) {
                    showLoading("Get data...")
                } else hideLoading()
            }

            state.error?.let {
                AuthHelper.specifyErrorMessage(this, it)
            }
        }

    }

}