package net.techbrewery.tvphotoframe.core.firestore

import net.techbrewery.tvphotoframe.core.extensions.parseGooglePhotosDate
import net.techbrewery.tvphotoframe.network.responses.MediaItemApiModel
import java.time.LocalDateTime

data class Photo(
    val id: String,
    val productUrl: String,
    val baseUrl: String,
    val mimeType: String,
    val fileName: String,
    val creationTime: LocalDateTime,
    val width: Int,
    val height: Int,
    val cameraMake: String?,
    val cameraModel: String?
//    val focalLength: Float,
//    val apertureFNumber: Float,
//    val isoEquivalent: Int,
//    val exposureTime: String,
) {
    companion object {
        fun with(apiModel: MediaItemApiModel): Photo = Photo(
            id = apiModel.id,
            productUrl = apiModel.productUrl,
            baseUrl = apiModel.baseUrl,
            mimeType = apiModel.mimeType,
            fileName = apiModel.filename,
            creationTime = apiModel.mediaMetadata.creationTime.parseGooglePhotosDate(),
            width = apiModel.mediaMetadata.width,
            height = apiModel.mediaMetadata.height,
            cameraMake = apiModel.mediaMetadata.photo?.cameraMake,
            cameraModel = apiModel.mediaMetadata.photo?.cameraModel
        )
    }
}
