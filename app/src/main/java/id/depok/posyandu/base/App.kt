package id.depok.posyandu.base

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import id.depok.posyandu.BuildConfig
import id.depok.posyandu.utils.Constant
import okhttp3.OkHttpClient
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        OkHttpClient.Builder()
            .addInterceptor(
                Constant.buildChucker(this)
            )
            .build()
    }
}