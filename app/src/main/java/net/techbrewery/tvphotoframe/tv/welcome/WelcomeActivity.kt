package net.techbrewery.tvphotoframe.tv.welcome

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.techbrewery.tvphotoframe.R
import net.techbrewery.tvphotoframe.core.BaseActivity
import net.techbrewery.tvphotoframe.core.logs.DevDebugLog
import net.techbrewery.tvphotoframe.core.ui.google.GoogleSignInButton
import net.techbrewery.tvphotoframe.core.ui.theme.AppTheme
import net.techbrewery.tvphotoframe.core.ui.theme.Spacing
import net.techbrewery.tvphotoframe.core.ui.theme.Typography
import net.techbrewery.tvphotoframe.network.OAuth2APi
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeActivity : BaseActivity() {

    private val viewModel by viewModel<WelcomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SignInContent(
                        onSignInClicked = {
//                            viewModel.onAuthorizeClicked()
                            startWebAuth()
                        }
                    )
                }
            }
        }
        setupStateObservers()
    }

    private fun startWebAuth() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(OAuth2APi.AUTH_URL.toString()))
        startActivity(browserIntent)
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


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        DevDebugLog.log("Intent received: $intent")
    }
}

@Composable
fun SignInContent(
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
        Title()
        SignInDisclaimer()
        GoogleSignInButton(onSignInClicked)
//        CredentialsTextFields(
//            email = email,
//            onEmailChanged = onEmailChanged,
//            password = password,
//            onPasswordChanged = onPasswordChanged
//        )
    }
}

@Composable
fun Title() {
    Text(
        text = stringResource(R.string.title_activity_welcome),
        style = Typography.titleLarge
    )
}

@Composable
fun SignInDisclaimer() {
    Text(
        text = "W końcu sie doigra", //FIXME
        style = Typography.bodyLarge,
        modifier = Modifier.padding(Spacing.Small)
    )
}

@Composable
fun CredentialsTextFields(
    email: String,
    onEmailChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(Spacing.Large)
    ) {
        TextField(
            value = email,
//            label = { Text("Email") }, //FIXME
            onValueChange = onEmailChanged
        )
        Spacer(Modifier.height(Spacing.Small))
        TextField(
            value = password,
//            label = { Text("Password") }, //FIXME
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = onPasswordChanged
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 1024,
    heightDp = 768,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun SignInContentDarkPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SignInContent()
        }
    }
}

@Preview
@Composable
fun DefaultPreview() = SignInContentDarkPreview()