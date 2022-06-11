package net.techbrewery.tvphotoframe.network

import net.techbrewery.tvphotoframe.network.responses.AlbumsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApi {

    @GET("albums")
    suspend fun getAlbums(@Query("access_token") accessToken: String): AlbumsResponse
}