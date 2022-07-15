package net.techbrewery.tvphotoframe.network

import net.techbrewery.tvphotoframe.network.requests.PhotosSearchRequestBody
import net.techbrewery.tvphotoframe.network.responses.AlbumsResponse
import net.techbrewery.tvphotoframe.network.responses.PhotosResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PhotosApi {

    companion object {

        //FIXME
        const val TEMP_FAMILY_AND_FRIENDS_HARDCODED_ID =
            "AOrZmo15rdAWQgmbcdie0nQwB4wC3kXeYeG13qRiO5lVwoLp2u0DIfCPpAKYL6j0u8BaNVkCREKW"
    }

    @GET("albums")
    suspend fun getAlbums(
        @Query("page_size") pageSize: Int = 20,
        @Query("pageToken") pageToken: String? = null
    ): AlbumsResponse

    @POST("mediaItems**search")
    suspend fun getPhotosInAlbum(@Body body: PhotosSearchRequestBody): PhotosResponse
}