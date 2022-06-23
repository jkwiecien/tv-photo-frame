package net.techbrewery.tvphotoframe.network.responses

data class DeviceCodeResponse(
    val device_code: String,
    val expires_in: Int,
    val interval: Int,
    val user_code: String,
    val verification_url: String
)
