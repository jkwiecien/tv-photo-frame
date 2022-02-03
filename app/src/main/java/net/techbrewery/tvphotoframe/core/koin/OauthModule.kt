package net.techbrewery.tvphotoframe.core.koin

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import net.techbrewery.tvphotoframe.BuildConfig
import org.koin.core.module.Module
import org.koin.dsl.module

object OauthModule {
    private val SCOPES = listOf("https://www.googleapis.com/auth/photoslibrary.readonly")
    
    val get: Module
        get() = module {
            single { GsonFactory() }
            single { NetHttpTransport() }
            single {
                GoogleAuthorizationCodeFlow.Builder(
                    get(),
                    get(),
                    BuildConfig.OAUTH_CLIENT_ID,
                    BuildConfig.OAUTH_CLIENT_SECRET,
                    SCOPES
                )
            }
        }
}