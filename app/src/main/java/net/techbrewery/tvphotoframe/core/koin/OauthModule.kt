package net.techbrewery.tvphotoframe.core.koin

import android.accounts.AccountManager
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import net.techbrewery.tvphotoframe.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object OauthModule {
    private val SCOPES = listOf("https://www.googleapis.com/auth/photoslibrary.readonly")

    val get: Module
        get() = module {
            single { AccountManager.get(androidContext()) }
            single {
                GoogleAuthorizationCodeFlow.Builder(
                    NetHttpTransport(),
                    GsonFactory(),
                    BuildConfig.OAUTH_CLIENT_ID,
                    BuildConfig.OAUTH_CLIENT_SECRET,
                    SCOPES
                ).build()
            }
        }
}