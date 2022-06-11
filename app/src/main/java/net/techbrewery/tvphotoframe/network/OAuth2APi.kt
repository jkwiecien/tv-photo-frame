package net.techbrewery.tvphotoframe.network

import net.techbrewery.tvphotoframe.BuildConfig
import net.techbrewery.tvphotoframe.network.requests.AccessTokenRequestBody
import net.techbrewery.tvphotoframe.network.requests.RefreshTokenPostBody
import net.techbrewery.tvphotoframe.network.responses.AuthTokenResponse
import okhttp3.HttpUrl
import retrofit2.http.Body
import retrofit2.http.POST

interface OAuth2APi {

    companion object {
        // space delimited list of scopes
        private const val SCOPES: String = "https://www.googleapis.com/auth/photoslibrary.readonly"

        val AUTH_URL: HttpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("accounts.google.com")
            .encodedPath("/o/oauth2/v2/auth")
            .addQueryParameter("client_id", BuildConfig.OAUTH_CLIENT_ID)
            .addQueryParameter("redirect_uri", BuildConfig.REDIRECT_URI)
            .addQueryParameter("response_type", "code")
            .addQueryParameter("scope", SCOPES)
            .build()
    }

//    @GET("https://accounts.google.com/o/oauth2/v2/auth")
//    suspend fun getAuthUrl(
//        @Query("client_id") clientId: String = BuildConfig.OAUTH_CLIENT_ID,
//        @Query("redirect_uri") redirectUri: String = BuildConfig.REDIRECT_URI,
//        @Query("response_type") responseType: String = "code",
//        @Query("scope") scope: String = SCOPES,
//    ): String

    @POST("token")
    suspend fun getAccessToken(@Body body: AccessTokenRequestBody): AuthTokenResponse

    @POST("token")
    suspend fun refreshAccessToken(@Body body: RefreshTokenPostBody): AuthTokenResponse
}