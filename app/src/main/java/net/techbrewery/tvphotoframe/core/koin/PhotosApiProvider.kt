package net.techbrewery.tvphotoframe.core.koin

import android.content.Context
import com.google.gson.GsonBuilder
import net.techbrewery.tvphotoframe.network.PhotosApi
import net.techbrewery.tvphotoframe.network.ToStringConverterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object PhotosApiProvider {

    private lateinit var okHttpClient: OkHttpClient
    lateinit var photosApi: PhotosApi
        private set

    private fun createCache(context: Context): Cache {
        val cacheDirectory = File(context.cacheDir, "HttpResponseCache")
        return Cache(cacheDirectory, 25L)
    }

    private fun createAuthHeaderInterceptor(accessToken: String) = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        chain.proceed(request)
    }

    private fun createUrlColonInterceptor() = Interceptor { chain ->
        val url = chain.request().url.toString()
            .replace("**", ":")
        val request = chain.request().newBuilder()
            .url(url)
            .build()
        chain.proceed(request)
    }

    fun initApi(
        context: Context,
        accessToken: String
    ) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        okHttpClient = OkHttpClient.Builder()
            .cache(createCache(context))
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(createAuthHeaderInterceptor(accessToken))
            .addInterceptor(createUrlColonInterceptor())
            .protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://photoslibrary.googleapis.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addConverterFactory(ToStringConverterFactory())
            .client(okHttpClient)
            .build()

        val api = retrofit.create(PhotosApi::class.java)
        photosApi = api
    }
}