package net.techbrewery.tvphotoframe.network.responses

enum class VideoProcessingStatus {
    UNSPECIFIED, PROCESSING, READY, FAILED
}

data class VideoApiModel(
    val cameraMake: String,
    val cameraModel: String,
    val fps: Float,
    val status: VideoProcessingStatus
)