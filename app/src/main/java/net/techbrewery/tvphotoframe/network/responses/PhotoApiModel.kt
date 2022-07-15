package net.techbrewery.tvphotoframe.network.responses

data class PhotoApiModel(
    val cameraMake: String,
    val cameraModel: String,
    val focalLength: Float,
    val apertureFNumber: Float,
    val isoEquivalent: Int,
    val exposureTime: String
)