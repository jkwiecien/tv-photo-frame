package net.techbrewery.tvphotoframe.network

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("https://www.googleapis.com/oauth2/v4/token")
    suspend fun getAuthToken(@Body body: AuthTokenPostBody): String
}