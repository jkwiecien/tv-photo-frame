package net.techbrewery.tvphotoframe.core.koin

import com.google.gson.GsonBuilder
import net.techbrewery.tvphotoframe.network.OAuth2APi
import net.techbrewery.tvphotoframe.network.ToStringConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object OAuth2Module {
    const val MODULE_NAME = "oauth2"

    val get: Module
        get() = module {
            single<OkHttpClient>(named(MODULE_NAME)) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
                OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build()
            }
            single<Retrofit>(named(MODULE_NAME)) {
                Retrofit.Builder()
                    .baseUrl("https://oauth2.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .addConverterFactory(ToStringConverterFactory())
                    .client(get(named(MODULE_NAME)))
                    .build()
            }
            single<OAuth2APi> {
                val retrofit: Retrofit = get(named(MODULE_NAME))
                retrofit.create(OAuth2APi::class.java)
            }
        }
}