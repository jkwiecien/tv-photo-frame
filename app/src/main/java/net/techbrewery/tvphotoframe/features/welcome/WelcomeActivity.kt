package net.techbrewery.tvphotoframe.features.welcome

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
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
import net.techbrewery.tvphotoframe.R
import net.techbrewery.tvphotoframe.core.ui.google.GoogleSignInButton
import net.techbrewery.tvphotoframe.core.ui.theme.AppTheme
import net.techbrewery.tvphotoframe.core.ui.theme.Spacing
import net.techbrewery.tvphotoframe.core.ui.theme.Typography
import org.koin.androidx.compose.viewModel

class WelcomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SignInContent()
                }
            }
        }
    }
}

@Composable
private fun SignInContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val vm by viewModel<WelcomeViewModel>()
        Title()
        SignInDisclaimer()
        CredentialsTextFields(vm)
        GoogleSignInButton()
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
        text = "In order to use Google Photos with TV Photo frame, you need to sign in into your Google account.", //FIXME
        style = Typography.bodyLarge,
        modifier = Modifier.padding(Spacing.Small)
    )
}

@Composable
fun CredentialsTextFields(vm: WelcomeViewModel) {
    Column(
        modifier = Modifier.padding(Spacing.Large)
    ) {
        TextField(
            value = vm.emailState,
//            label = { Text("Email") }, //FIXME
            onValueChange = { vm.setEmail(it) }
        )
        Spacer(Modifier.size(Spacing.Small))
        TextField(
            value = vm.passwordState,
//            label = { Text("Password") }, //FIXME
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { vm.setPassword(it) }
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