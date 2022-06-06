package net.techbrewery.tvphotoframe.network

data class AlbumsResponse(
    val albums: List<AlbumApiModel>,
    val nextPageToken: String
)