package net.techbrewery.tvphotoframe.mobile.welcome

import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import org.koin.androidx.viewmodel.ext.android.viewModel


class WelcomeMobileActivity : BaseActivity() {

    private val viewModel by viewModel<WelcomeMobileViewModel>()

    private val startAccountPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                data?.extras?.logAllExtras()
                val accountName = data?.extras?.get(AccountManager.KEY_ACCOUNT_NAME)
                DevDebugLog.log("Selected account: $accountName")
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
                            onSignInClicked = { startAuth() }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_OK && requestCode == RequestCodes.GOOGLE_PHOTOS_AUTH) {
            DevDebugLog.log("Authorized Google Photos")
        }
    }

    private fun requestAccountsPicker() {
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
}