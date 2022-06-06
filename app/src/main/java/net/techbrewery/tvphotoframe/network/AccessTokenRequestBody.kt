package net.techbrewery.tvphotoframe.network

import net.techbrewery.tvphotoframe.BuildConfig

data class AccessTokenRequestBody(
    val code: String,
    val grant_type: String = "authorization_code",
    val client_id: String = BuildConfig.OAUTH_WEB_CLIENT_ID,
    val client_secret: String = BuildConfig.OAUTH_WEB_CLIENT_SECRET,
    val redirect_uri: String = BuildConfig.REDIRECT_URI
)
