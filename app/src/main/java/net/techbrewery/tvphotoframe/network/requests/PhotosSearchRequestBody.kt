package net.techbrewery.tvphotoframe.network.requests

import androidx.annotation.Keep

@Keep
data class PhotosSearchRequestBody(
    val albumId: String,
    val pageSize: Int = 100,
    val pageToken: String? = null,
    val filters: FiltersRequestBody? = null
)

@Keep
data class FiltersRequestBody(
    val mediaTypeFilter: MediaTypeFilterRequestBody = MediaTypeFilterRequestBody(),
    val includeArchivedMedia: Boolean = false,
    val excludeNonAppCreatedData: Boolean = false
)

@Keep
data class MediaTypeFilterRequestBody(
    val mediaTypes: List<String> = listOf("PHOTO")
)