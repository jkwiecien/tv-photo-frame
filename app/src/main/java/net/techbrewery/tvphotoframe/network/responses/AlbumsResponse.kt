package net.techbrewery.tvphotoframe.network.responses

data class AlbumsResponse(
    val albums: List<AlbumApiModel>,
    val nextPageToken: String
)