package id.depok.posyandu.data.services.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import android.content.Context
import id.depok.posyandu.data.preferences.AppPreference


class ReceivedCookiesInterceptor(private val context: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            val cookies = HashSet<String>()
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }
            // Save the cookies (I saved in SharedPrefrence), you save whereever you want to save
            AppPreference.saveStringSet(context, AppPreference.PREF_KEY_COOKIES, cookies)


        }

        return originalResponse
    }

}