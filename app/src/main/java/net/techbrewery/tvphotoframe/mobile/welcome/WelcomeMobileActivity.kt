package net.techbrewery.tvphotoframe.mobile.welcome

import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import net.techbrewery.tvphotoframe.BuildConfig
import net.techbrewery.tvphotoframe.core.BaseActivity
import net.techbrewery.tvphotoframe.core.RequestCodes
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog
import net.techbrewery.tvphotoframe.core.ui.components.MobileViewRoot
import net.techbrewery.tvphotoframe.core.ui.components.PrimaryButton
import net.techbrewery.tvphotoframe.core.ui.google.GoogleSignInButton
import net.techbrewery.tvphotoframe.core.ui.theme.SpacingMobile
import net.techbrewery.tvphotoframe.mobile.photos.GallerySyncActivity
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
//        oneTapClient = Identity.getSignInClient(this)
//        firebaseAuth = Firebase.auth

        GallerySyncActivity.start(this)
    }

//    override fun onResume() {
//        super.onResume()
//        tryResumingFirebaseUser()
//    }

    private fun displayAuthUi() {
        setContent {
            MobileViewRoot {
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
            MobileViewRoot {
                WelcomeContent(
                    onSyncPhotosClicked = {
                        GallerySyncActivity.start(this)
                    }
                )
            }
        }
        DevDebugLog.log("User resumed")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCodes.FIREBASE_GOOGLE_AUTH) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                val password = credential.password
                if (idToken != null) {
                    Timber.d("Got ID token.")
                    firebaseAuthWithIdToken(idToken)
                } else if (password != null) {
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
            .padding(SpacingMobile.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GoogleSignInButton(onSignInClicked)
    }
}

@Composable
private fun WelcomeContent(
    onSyncPhotosClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(SpacingMobile.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Hello. What's cookin?")
        SpacingMobile.LargeSpacingBox()
        PrimaryButton(
            title = "Sync photos",
            onClick = onSyncPhotosClicked
        )
        SpacingMobile.SmallSpacingBox()
        PrimaryButton(
            title = "Connect TV",
            onClick = {
                //TODO
            }
        )
    }
}

@Preview
    (showBackground = true)
@Composable
private fun WelcomeContentPreview() {
    MobileViewRoot {
        WelcomeContent()
    }
}