package net.techbrewery.tvphotoframe.core.koin

import android.content.Context
import com.google.gson.GsonBuilder
import net.techbrewery.tvphotoframe.network.PhotosApi
import net.techbrewery.tvphotoframe.network.ToStringConverterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object PhotosApiModule {
    const val MODULE_NAME = "photos"

    private fun createCache(context: Context): Cache {
        val cacheDirectory = File(context.cacheDir, "HttpResponseCache")
        return Cache(cacheDirectory, 25L)
    }

    val get: Module
        get() = module {
            factory<OkHttpClient>(named(MODULE_NAME)) {
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
            factory<Retrofit>(named(MODULE_NAME)) {
                Retrofit.Builder()
                    .baseUrl("https://photoslibrary.googleapis.com/v1/")
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .addConverterFactory(ToStringConverterFactory())
                    .client(get(named(MODULE_NAME)))
                    .build()
            }
            factory<PhotosApi>(named(MODULE_NAME)) {
                val retrofit: Retrofit = get(named(MODULE_NAME))
                retrofit.create(PhotosApi::class.java)
            }
        }
}