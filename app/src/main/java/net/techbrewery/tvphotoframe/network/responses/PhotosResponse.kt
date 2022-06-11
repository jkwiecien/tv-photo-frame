package net.techbrewery.tvphotoframe.network.responses

data class PhotosResponse(
    val mediaItems: List<MediaItemApiModel>,
    val nextPageToken: String
)
