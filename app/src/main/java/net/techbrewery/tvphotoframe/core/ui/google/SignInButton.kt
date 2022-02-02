package net.techbrewery.tvphotoframe.core.ui.google

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.techbrewery.tvphotoframe.R
import net.techbrewery.tvphotoframe.core.ui.theme.AppTheme
import net.techbrewery.tvphotoframe.core.ui.theme.Spacing
import net.techbrewery.tvphotoframe.core.ui.theme.Typography

@Composable
fun GoogleSignInButton() {
    Button(
        modifier = Modifier.padding(Spacing.ButtonPadding),
        onClick = {
            //TODO
        }
    ) {
        Image(
            painter = painterResource(R.drawable.ic_google_logo),
            contentDescription = "Google logo",
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = "Sign in with Google",
            style = Typography.labelLarge,
            modifier = Modifier.padding(Spacing.ButtonPadding)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun DefaultPreview() {
    AppTheme {
        GoogleSignInButton()
    }
}