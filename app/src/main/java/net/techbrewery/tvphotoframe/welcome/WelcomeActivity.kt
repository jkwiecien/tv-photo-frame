package net.techbrewery.tvphotoframe.welcome

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.techbrewery.tvphotoframe.R
import net.techbrewery.tvphotoframe.ui.google.GoogleSignInButton
import net.techbrewery.tvphotoframe.ui.theme.AppTheme
import net.techbrewery.tvphotoframe.ui.theme.Spacing
import net.techbrewery.tvphotoframe.ui.theme.Typography

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
        Title()
        SignInDisclaimer()
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

@Preview(
    showBackground = true,
    widthDp = 1920,
    heightDp = 1080,
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