package net.techbrewery.tvphotoframe.mobile.welcome

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
<<<<<<< HEAD
import androidx.activity.compose.setContent
=======
>>>>>>> attempt-web
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.techbrewery.tvphotoframe.BuildConfig
import net.techbrewery.tvphotoframe.core.BaseActivity
import net.techbrewery.tvphotoframe.core.RequestCodes
import net.techbrewery.tvphotoframe.core.extensions.logAllExtras
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog
import net.techbrewery.tvphotoframe.network.AccessTokenRequestBody
import net.techbrewery.tvphotoframe.network.AuthApi
import net.techbrewery.tvphotoframe.tv.welcome.GoogleAuthUrlReceived
<<<<<<< HEAD
=======
import okhttp3.HttpUrl
>>>>>>> attempt-web
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class WelcomeMobileActivity : BaseActivity() {

    companion object {
        private val AUTH_URL = HttpUrl.Builder()
            .scheme("https")
            .host("accounts.google.com")
            .encodedPath("/o/oauth2/v2/auth")
            .addQueryParameter("client_id", BuildConfig.OAUTH_CLIENT_ID)
            .addQueryParameter("redirect_uri", BuildConfig.REDIRECT_URI)
            .addQueryParameter("response_type", "code")
            .addQueryParameter("scope", "https://www.googleapis.com/auth/photoslibrary.readonly")
            .build()
    }

    private val viewModel by viewModel<WelcomeMobileViewModel>()
    private val accountManager by inject<AccountManager>()

    private val startAccountPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                data?.extras?.logAllExtras()
                val accountName: String = data?.extras?.getString(AccountManager.KEY_ACCOUNT_NAME)!!
                val accountType: String = data.extras?.getString(AccountManager.KEY_ACCOUNT_TYPE)!!
                DevDebugLog.log("Selected account: $accountName with type: $accountType")
                onAccountPicked(accountName, accountType)
<<<<<<< HEAD
=======
            }
        }

    private val startSignIn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val gsa = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    .getResult(ApiException::class.java)
                onLoggedIn(gsa)

//                GoogleSignIn.requestPermissions(
//                    this,
//                    RequestCodes.GOOGLE_PHOTOS_AUTH,
//                    GoogleSignIn.getLastSignedInAccount(this),
//                    Scope("https://www.googleapis.com/auth/photoslibrary.readonly")
//                )
>>>>>>> attempt-web
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        GoogleSignInButton(
                            onSignInClicked = { startAuth2() }
                        )
                    }
                }
=======

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL.toString()))
        startActivity(browserIntent)

//        val gso = GoogleSignIn.getLastSignedInAccount(this)
//
//        setContent {
//            AppTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Column {
//                        if (gso == null) {
//                            GoogleSignInButton(
//                                onSignInClicked = { startAuth() }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        if (gso != null) onLoggedIn2(gso)
    }

    private fun onLoggedIn(gso: GoogleSignInAccount) {
        val idToken = gso.idToken
        DevDebugLog.log("ID token: $idToken")
        if (idToken != null) {
            lifecycleScope.launch(Dispatchers.Main) {
                DevDebugLog.log("Sending id token: $idToken")
                val api by inject<AuthApi>()
                withContext(Dispatchers.IO) { api.getAccessToken(AccessTokenRequestBody(idToken)) }
            }
        }
    }

    private fun onLoggedIn2(gso: GoogleSignInAccount) {
        val idToken = gso.idToken
        DevDebugLog.log("ID token: $idToken")

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseCredential

        if (idToken != null) {
            lifecycleScope.launch(Dispatchers.Main) {
                DevDebugLog.log("Sending id token: $idToken")
                val api by inject<AuthApi>()
                val authUrl = withContext(Dispatchers.IO) { api.getAUthUrl() }
                DevDebugLog.log("Auth url: $authUrl")
>>>>>>> attempt-web
            }
        }
    }

    private fun setupStateObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.eventsFlow.collect { event ->
                when (val payload = event.getContentIfNotHandled()) {
                    is GoogleAuthUrlReceived -> {
                        val url = payload.url
                        DevDebugLog.log("authUrl observed: ${payload.url}")
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun startAuth() {
        val gso = GoogleSignInOptions.Builder()
            .requestScopes(Scope("https://www.googleapis.com/auth/photoslibrary.readonly"))
            .requestIdToken(BuildConfig.OAUTH_WEB_CLIENT_ID)
            .build()

        val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
        startSignIn.launch(signInIntent)
    }

    private fun startAuth2() {
        val intent = AccountManager.newChooseAccountIntent(
            null,
            null,
            arrayOf("com.google"),
            null,
            null,
            null,
            null
        )
        DevDebugLog.log("Starting account picker")
        startAccountPicker.launch(intent)
    }

    private fun onAccountPicked(accountName: String, accountType: String) {
        val account = Account(accountName, accountType)
        val onTokenAcquired = AccountManagerCallback<Bundle> { result ->
            DevDebugLog.log("On token result acquired")
            // Get the result of the operation from the AccountManagerFuture.
            val resultType = result.result
            val bundle: Bundle = result.result

            // The token is a named value in the bundle. The name of the value
            // is stored in the constant AccountManager.KEY_AUTHTOKEN.
            val token: String? = bundle.getString(AccountManager.KEY_AUTHTOKEN)
            DevDebugLog.log("Token acquired: $token")
        }

        val onError = Handler(
            Looper.getMainLooper()
        ) {
            DevDebugLog.log("Handler: Error while trying to auth")
            true
        }


        accountManager.getAuthToken(
            account,                     // Account retrieved using getAccountsByType()
            "https://www.googleapis.com/auth/photoslibrary.readonly",
            Bundle(),                        // Authenticator-specific options
            this,                           // Your activity
            onTokenAcquired,              // Callback called when a token is successfully acquired
            onError            // Callback called if an error occurs
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
<<<<<<< HEAD
        if (requestCode == RESULT_OK && requestCode == RequestCodes.GOOGLE_PHOTOS_AUTH) {
            DevDebugLog.log("Authorized Google Photos")
=======

        if (resultCode == RESULT_OK && requestCode == RequestCodes.GOOGLE_PHOTOS_AUTH && data != null) {
            DevDebugLog.log("Authorized Google Photos")
            val gsa =
                GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)
            DevDebugLog.log("ID token: ${gsa.idToken}")
//            DevDebugLog.log("Data in result: ${data?.extras?.logAllExtras()}")
>>>>>>> attempt-web
        }
    }

}