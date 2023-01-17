package id.depok.posyandu.data.services.interceptor

import android.content.Context
import android.util.Log
import id.depok.posyandu.data.preferences.AppPreference
import okhttp3.Interceptor
import okhttp3.Response



class AddCookiesInterceptor (private val context: Context): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val preferences = AppPreference.getStringSet(context, AppPreference.PREF_KEY_COOKIES)
        preferences?.forEach {
            builder.addHeader("Cookie", it)
        }
        return chain.proceed(builder.build())
    }

}