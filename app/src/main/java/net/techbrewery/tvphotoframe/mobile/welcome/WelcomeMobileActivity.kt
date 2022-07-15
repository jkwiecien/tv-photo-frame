package net.techbrewery.tvphotoframe.mobile.welcome

import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.techbrewery.tvphotoframe.BuildConfig
import net.techbrewery.tvphotoframe.core.BaseActivity
import net.techbrewery.tvphotoframe.core.BundleKeys
import net.techbrewery.tvphotoframe.core.RequestCodes
import net.techbrewery.tvphotoframe.core.koin.PhotosApiProvider
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog
import net.techbrewery.tvphotoframe.core.ui.components.ViewRoot
import net.techbrewery.tvphotoframe.core.ui.google.GoogleSignInButton
import net.techbrewery.tvphotoframe.core.ui.theme.Spacing
import net.techbrewery.tvphotoframe.network.OAuth2APi.Companion.AUTH_URL
import net.techbrewery.tvphotoframe.network.PhotosApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class WelcomeMobileActivity : BaseActivity() {

    private val viewModel by viewModel<WelcomeMobileViewModel>()
    private val sharedPreferences: SharedPreferences by inject()

    private lateinit var oneTapClient: SignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oneTapClient = Identity.getSignInClient(this)
        firebaseAuth = Firebase.auth
    }

    override fun onResume() {
        super.onResume()
        tryResumingFirebaseUser()
    }

    private fun displayAuthUi() {
        setContent {
            ViewRoot {
                AuthContent(
                    onSignInClicked = { startGoogleAuth() }
                )
            }
        }
    }

    private fun tryResumingFirebaseUser() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            DevDebugLog.log("Already logged in")
            onUserResumed(user)
        } else {
            DevDebugLog.log("Not logged in")
            displayAuthUi()
        }
    }

    private fun startGoogleAuth() {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.OAUTH_WEB_CLIENT_ID)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender,
                        RequestCodes.FIREBASE_GOOGLE_AUTH,
                        null,
                        0,
                        0,
                        0
                    )
                } catch (e: SendIntentException) {
                    Timber.e("Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener { error ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Timber.d("No saved credentials found ${error.localizedMessage}");
            }
    }

    private fun firebaseAuthWithIdToken(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser!!
                    onUserResumed(user)
                } else {
                    onFirebaseAuthFailed(task.exception)
                }
            }
            .addOnFailureListener { error ->
                onFirebaseAuthFailed(error)
            }
    }

    private fun onFirebaseAuthFailed(error: Throwable?) {
        //TODO update ui
        Timber.e("Couldn't auth Firebase with idToken", error)
    }

    private fun onUserResumed(user: FirebaseUser) {
        setContent {
            ViewRoot {
                WelcomeContent()
            }
        }
        DevDebugLog.log("User resumed")
        checkPhotosAuth()
    }

    private fun checkPhotosAuth() {
        val accessToken = sharedPreferences.getString(BundleKeys.PHOTOS_ACCESS_TOKEN, null)
        if (accessToken != null) {
            PhotosApiProvider.initApi(this, accessToken)
            fetchPhotos()
        } else {
            authPhotos()
        }
    }

    private fun fetchPhotos() {
        lifecycleScope.launch(Dispatchers.Main) {
            val photosOfFamily = withContext(Dispatchers.IO) {
                PhotosApiProvider.photosApi.getPhotosInAlbum(PhotosApi.TEMP_FAMILY_AND_FRIENDS_HARDCODED_ID)
            }
            photosOfFamily.mediaItems.forEach { mediaItem ->
                DevDebugLog.log("Photo found: ${mediaItem.productUrl}")
            }
        }
    }

    private fun authPhotos() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL.toString()))
        startActivity(browserIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCodes.FIREBASE_GOOGLE_AUTH) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
//                val username = credential.id
                val password = credential.password
                if (idToken != null) {
                    Timber.d("Got ID token.")
                    firebaseAuthWithIdToken(idToken)
                } else if (password != null) {
                    // Got a saved username and password. Use them to authenticate
                    // with your backend.
                    Timber.d("Got password.")
                }
            } catch (error: ApiException) {
                Timber.e(error)
            }
        }
    }

}

@Composable
private fun AuthContent(
    onSignInClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GoogleSignInButton(onSignInClicked)
    }
}

@Composable
private fun WelcomeContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Fetching photos...")
    }
}