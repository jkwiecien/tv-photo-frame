package net.techbrewery.tvphotoframe.network

import net.techbrewery.tvphotoframe.BuildConfig
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("https://oauth2.googleapis.com/token")
    suspend fun getAccessToken(@Body body: AccessTokenRequestBody): AuthTokenResponse

    @POST("https://oauth2.googleapis.com/token")
    suspend fun refreshAccessToken(@Body body: RefreshTokenPostBody): AuthTokenResponse

    @GET("https://accounts.google.com/o/oauth2/v2/auth")
    suspend fun getAUthUrl(
        @Query("client_id") clientId: String = BuildConfig.OAUTH_CLIENT_ID,
        @Query("redirect_uri") redirectUri: String = BuildConfig.REDIRECT_URI,
        @Query("response_type") responseType: String = "code",
        @Query("scope") scope: String = "https://www.googleapis.com/auth/photoslibrary.readonly",
    ): String

    @GET("https://photoslibrary.googleapis.com/v1/albums")
    suspend fun getAlbums(@Query("access_token") accessToken: String): AlbumsResponse

}