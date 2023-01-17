package id.depok.posyandu.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import id.depok.posyandu.R
import id.depok.posyandu.utils.DialogGenerator
import kotlinx.android.synthetic.main.dialog_loading.*

abstract class BaseActivity : AppCompatActivity() {

    val dialogLoading by lazy {
        val dialog = Dialog(this)
        val inflate = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)
        dialog.apply {
            setCancelable(false)
            setContentView(inflate)
            window?.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            val window = this.window
            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.statusBarColor =
                this.let { ContextCompat.getColor(it, R.color.colorAccent) }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                val decor = window.decorView
                decor.systemUiVisibility =
                    decor.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window?.statusBarColor =
                    this.let { ContextCompat.getColor(it, R.color.colorAccent) }

            }
        }
    }

    fun showConnectionWarningDialog() {
        DialogGenerator.generateConfirmationDialog(
            context = this,
            title = "Tidak ada koneksi Internet",
            message = "Perangkat anda tidak terhubung ke internet",
            cancelAble = false,
            textButton = "Coba lagi"
        ) {
            //dialogConfirmation = it
            if (!networkChecker()) showConnectionWarningDialog()
            else {
                recreate()
            }
        }
    }

    private fun networkChecker(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline()
        } else {
            var status = false
            try {
                val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var netInfo = cm.getNetworkInfo(0)
                when {
                    netInfo != null && netInfo.state == NetworkInfo.State.CONNECTED -> status = true
                    else -> {
                        netInfo = cm.getNetworkInfo(1)
                        if (netInfo != null && netInfo.state == NetworkInfo.State.CONNECTED) status =
                            true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
            return status
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                //Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                // Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                //Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

    fun showLoading(msg: String) {
        runOnUiThread {
            if (networkChecker()) {
                try {
                    dialogLoading.tvMessageLoading.text = msg
                    dialogLoading.show()
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            } else {
                showConnectionWarningDialog()
            }
        }
    }

    fun hideLoading() {
        runOnUiThread {
            if (dialogLoading.isShowing) {
                dialogLoading.dismiss()
            }
        }
    }

    fun updateMessageLoading(msg: String) {
        runOnUiThread {
            if (dialogLoading.isShowing) {
                dialogLoading.tvMessageLoading.text = msg
            }
        }
    }
}