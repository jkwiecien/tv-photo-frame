package net.techbrewery.tvphotoframe.tv.welcome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import net.techbrewery.tvphotoframe.BuildConfig
import net.techbrewery.tvphotoframe.core.BaseViewModel
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog


class WelcomeViewModel(
    private val googleAuthCodeFlow: GoogleAuthorizationCodeFlow
) : BaseViewModel() {
    var emailState by mutableStateOf("jacek.kwiecien@gmail.com")
        private set

    fun setEmail(email: String) {
        if (emailState != email) emailState = email
    }

    var passwordState by mutableStateOf("")
        private set

    fun setPassword(password: String) {
        if (passwordState != password) passwordState = password
    }

    fun onSignInClicked() {
        val credential = googleAuthCodeFlow.loadCredential(emailState)
        if (credential != null) {
            DevDebugLog.log("Credential found: $credential")
            //TODO
        } else {
            DevDebugLog.log("No credentials were found. Firing newAuthorizationUrl()")
            val authUrl = googleAuthCodeFlow.newAuthorizationUrl()
            authUrl.redirectUri = BuildConfig.REDIRECT_URI
            DevDebugLog.log("Auth url: $authUrl")
            sendEvent(GoogleAuthUrlReceived(authUrl.toString()))
        }

//        val credential: GoogleCredential = GoogleCredential().setAccessToken(accessToken)
//        val plus: Plus = builder(
//            NetHttpTransport(),
//            GsonFactory.getDefaultInstance(),
//            credential
//        )
//            .setApplicationName("Google-PlusSample/1.0")
//            .build()
//
//        val settings = PhotosLibrarySettings.newBuilder()
//            .setCredentialsProvider(
//                FixedCredentialsProvider.create(Credentials())
//            )
//            .build()
    }
}