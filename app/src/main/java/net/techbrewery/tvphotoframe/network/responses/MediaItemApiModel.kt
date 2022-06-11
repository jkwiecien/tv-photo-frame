package net.techbrewery.tvphotoframe.network.responses

data class MediaItemApiModel(
    val id: String,
    val description: String?,
    val productUrl: String,
    val baseUrl: String,
    val mimeType: String,
    val filename: String,
    val mediaMetadata: MediaMetaDataApiModel,
    val contributorInfo: ContributorInfoApiModel
)

