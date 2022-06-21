package net.techbrewery.tvphotoframe.mobile.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.techbrewery.tvphotoframe.core.BaseActivity
import net.techbrewery.tvphotoframe.core.koin.OAuth2Module
import net.techbrewery.tvphotoframe.core.koin.PhotosApiProvider
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog
import net.techbrewery.tvphotoframe.network.OAuth2APi
import net.techbrewery.tvphotoframe.network.PhotosApi
import net.techbrewery.tvphotoframe.network.requests.AccessTokenRequestBody
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class MobileAuthActivity : BaseActivity() {

    private val authApi: OAuth2APi by inject(named(OAuth2Module.MODULE_NAME))

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, MobileAuthActivity::class.java)
            activity.startActivity(intent)
        }
    }

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

//            val albumsResponse: AlbumsResponse =
//                withContext(Dispatchers.IO) { photosApi.getAlbums(accessTokenResponse.access_token) }
//            albumsResponse.albums.forEach { album -> DevDebugLog.log("Album found: ${album.title}. id: ${album.id}") }


            val photosApi =
                PhotosApiProvider.getApi(this@MobileAuthActivity, accessTokenResponse.access_token)
            val photosOfFamily = withContext(Dispatchers.IO) {
                photosApi.getPhotosInAlbum(PhotosApi.TEMP_FAMILY_AND_FRIENDS_HARDCODED_ID)
            }
            photosOfFamily.mediaItems.forEach { mediaItem ->
                DevDebugLog.log("Photo found: ${mediaItem.productUrl}")
            }
        }
    }

}