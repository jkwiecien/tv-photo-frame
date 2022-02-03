package net.techbrewery.tvphotoframe.features.welcome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.Credentials
import com.google.photos.library.v1.PhotosLibrarySettings
import com.sun.org.apache.xpath.internal.operations.Plus
import net.techbrewery.tvphotoframe.core.BaseViewModel


class WelcomeViewModel : BaseViewModel() {
    var emailState by mutableStateOf("")
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
        val credential: GoogleCredential = GoogleCredential().setAccessToken(accessToken)
        val plus: Plus = builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName("Google-PlusSample/1.0")
            .build()

        val settings = PhotosLibrarySettings.newBuilder()
            .setCredentialsProvider(
                FixedCredentialsProvider.create(Credentials())
            )
            .build()
    }
}