package net.techbrewery.tvphotoframe.network.responses

data class MediaMetaDataApiModel(
    val creationTime: String,
    val width: Int,
    val height: Int,
    val photo: PhotoApiModel?,
    val video: VideoApiModel?
)