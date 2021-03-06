package net.techbrewery.tvphotoframe.network.requests

import net.techbrewery.tvphotoframe.BuildConfig

data class RefreshTokenPostBody(
    val refresh_token: String,
    val grant_type: String = "authorization_code",
    val client_id: String = BuildConfig.OAUTH_WEB_CLIENT_ID,
    val client_secret: String = BuildConfig.OAUTH_WEB_CLIENT_SECRET
)
