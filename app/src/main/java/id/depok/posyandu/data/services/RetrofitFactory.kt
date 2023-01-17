package id.depok.posyandu.data.services

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.CookieCache
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import id.depok.posyandu.data.services.interceptor.ReceivedCookiesInterceptor
import id.depok.posyandu.BuildConfig
import id.depok.posyandu.utils.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RetrofitFactory {

    fun getRetrofitService(context: Context): IRetrofitService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val cookiesInterceptor = ReceivedCookiesInterceptor(context)
        val cookieJar = PersistentCookieJar(
            SetCookieCache() as CookieCache,
            SharedPrefsCookiePersistor(context)
        )

        val client =
            getUnsafeOkHttpClient(context).connectTimeout(500, TimeUnit.SECONDS)
                .readTimeout(500, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(Constant.buildChucker(context))
                .addInterceptor(cookiesInterceptor)
                .cookieJar(cookieJar)
                .addInterceptor(Interceptor {
                    return@Interceptor it.proceed(
                        it.request()
                            .newBuilder()
                            .addHeader("Accept", "application/json")
                            .build()
                    )
                })
                .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build().create(IRetrofitService::class.java)
    }

    fun getFireBaseService(context: Context): IRetrofitService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val cookiesInterceptor = ReceivedCookiesInterceptor(context)
        val cookieJar = PersistentCookieJar(
            SetCookieCache() as CookieCache,
            SharedPrefsCookiePersistor(context)
        )

        val client =
            getUnsafeOkHttpClient(context).connectTimeout(500, TimeUnit.SECONDS)
                .readTimeout(500, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(Constant.buildChucker(context))
                .addInterceptor(cookiesInterceptor)
                .cookieJar(cookieJar)
                .addInterceptor(Interceptor {
                    return@Interceptor it.proceed(
                        it.request()
                            .newBuilder()
                            .addHeader("Accept", "application/json")
                            .build()
                    )
                })
                .build()

        return Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/fcm/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build().create(IRetrofitService::class.java)
    }

    private fun getUnsafeOkHttpClient(context: Context): OkHttpClient.Builder {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts.first() as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }

            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            okhttp3.MultipartBody.FORM, descriptionString
        )
    }
}