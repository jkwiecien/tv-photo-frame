package net.techbrewery.tvphotoframe.network.responses

data class AuthTokenResponse(
    val access_token: String,
    val expires_in: Int,
    val id_token: String?,
    val refresh_token: String,
    val scope: String,
    val token_type: String
)
