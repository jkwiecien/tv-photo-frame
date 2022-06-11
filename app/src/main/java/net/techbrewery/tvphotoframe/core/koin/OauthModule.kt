package net.techbrewery.tvphotoframe.core.koin

import android.accounts.AccountManager
import android.content.Context
import com.google.gson.GsonBuilder
import net.techbrewery.tvphotoframe.network.AuthApi
import net.techbrewery.tvphotoframe.network.ToStringConverterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


object OauthModule {
    private fun createCache(context: Context): Cache {
        val cacheDirectory = File(context.cacheDir, "HttpResponseCache")
        return Cache(cacheDirectory, 25L)
    }

//    private val SCOPES = listOf("https://www.googleapis.com/auth/photoslibrary.readonly")

    val get: Module
        get() = module {
            single<AccountManager> { AccountManager.get(androidContext()) }
//            single {
//                GoogleAuthorizationCodeFlow.Builder(
//                    NetHttpTransport(),
//                    GsonFactory(),
//                    BuildConfig.OAUTH_CLIENT_ID,
//                    BuildConfig.OAUTH_WEB_CLIENT_SECRET,
//                    SCOPES
//                ).build()
//            }
            single<OkHttpClient> {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }

                OkHttpClient.Builder()
                    .cache(createCache(androidContext()))
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build()
            }
            single<Retrofit> {
                Retrofit.Builder()
                    .baseUrl("http://localhost/")
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder()
//                                .setLenient()
                                .create()
                        )
                    )
                    .addConverterFactory(ToStringConverterFactory())
                    .client(get())
                    .build()
            }
            single<AuthApi> {
                val retrofit: Retrofit = get()
                retrofit.create(AuthApi::class.java)
            }
        }
}