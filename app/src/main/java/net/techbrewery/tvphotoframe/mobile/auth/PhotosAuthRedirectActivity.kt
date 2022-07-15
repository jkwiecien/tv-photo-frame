package net.techbrewery.tvphotoframe.mobile.auth

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.techbrewery.tvphotoframe.core.BaseActivity
import net.techbrewery.tvphotoframe.core.BundleKeys
import net.techbrewery.tvphotoframe.core.koin.PhotosApiProvider
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog
import net.techbrewery.tvphotoframe.network.OAuth2APi
import net.techbrewery.tvphotoframe.network.requests.AccessTokenRequestBody
import org.koin.android.ext.android.inject

class PhotosAuthRedirectActivity : BaseActivity() {

    private val authApi: OAuth2APi by inject()
    private val sharedPreferences: SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DevDebugLog.log("Deep link intercepted: ${intent.data}")
        handleAuthDeepLink(intent.data!!)
    }

    private fun handleAuthDeepLink(data: Uri) {
        lifecycleScope.launch(Dispatchers.Main) {
            val authCode = data.getQueryParameter("code")!!
            val accessTokenResponse =
                withContext(Dispatchers.IO) { authApi.getAccessToken(AccessTokenRequestBody(authCode)) }
            DevDebugLog.log("Auth token received: $accessTokenResponse")
            sharedPreferences.edit()
                .putString(BundleKeys.PHOTOS_ACCESS_TOKEN, accessTokenResponse.access_token)
                .putString(BundleKeys.PHOTOS_REFRESH_TOKEN, accessTokenResponse.refresh_token)
                .commit()
            PhotosApiProvider.initApi(
                this@PhotosAuthRedirectActivity,
                accessTokenResponse.access_token
            )
            finish()

//            val albumsResponse: AlbumsResponse =
//                withContext(Dispatchers.IO) { photosApi.getAlbums(accessTokenResponse.access_token) }
//            albumsResponse.albums.forEach { album -> DevDebugLog.log("Album found: ${album.title}. id: ${album.id}") }


//            val photosApi =
//                PhotosApiProvider.getApi(
//                    this@PhotosAuthRedirectActivity,
//                    accessTokenResponse.access_token
//                )
//            val photosOfFamily = withContext(Dispatchers.IO) {
//                photosApi.getPhotosInAlbum(PhotosApi.TEMP_FAMILY_AND_FRIENDS_HARDCODED_ID)
//            }
//            photosOfFamily.mediaItems.forEach { mediaItem ->
//                DevDebugLog.log("Photo found: ${mediaItem.productUrl}")
//            }
        }
    }

}