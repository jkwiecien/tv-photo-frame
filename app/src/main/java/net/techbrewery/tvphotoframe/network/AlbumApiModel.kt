package net.techbrewery.tvphotoframe.network

data class AlbumApiModel(
    val coverPhotoBaseUrl: String,
    val coverPhotoMediaItemId: String,
    val id: String,
    val isWriteable: String,
    val mediaItemsCount: String,
    val productUrl: String,
    val title: String
)