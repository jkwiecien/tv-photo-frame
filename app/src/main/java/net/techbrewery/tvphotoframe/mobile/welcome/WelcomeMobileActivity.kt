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
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.techbrewery.tvphotoframe.core.BaseActivity
import net.techbrewery.tvphotoframe.core.RequestCodes
import net.techbrewery.tvphotoframe.core.extensions.logAllExtras
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog
import net.techbrewery.tvphotoframe.core.ui.google.GoogleSignInButton
import net.techbrewery.tvphotoframe.core.ui.theme.AppTheme
import net.techbrewery.tvphotoframe.tv.welcome.GoogleAuthUrlReceived
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class WelcomeMobileActivity : BaseActivity() {

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
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            }
        }
        setupStateObservers()
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
        GoogleSignIn.requestPermissions(
            this,
            RequestCodes.GOOGLE_PHOTOS_AUTH,
            GoogleSignIn.getLastSignedInAccount(this),
            Scope("https://www.googleapis.com/auth/photoslibrary.readonly")
        )
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
        if (requestCode == RESULT_OK && requestCode == RequestCodes.GOOGLE_PHOTOS_AUTH) {
            DevDebugLog.log("Authorized Google Photos")
        }
    }

}