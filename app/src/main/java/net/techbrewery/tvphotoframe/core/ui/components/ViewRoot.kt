package net.techbrewery.tvphotoframe.core.ui.components

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import net.techbrewery.tvphotoframe.core.ui.theme.MobileTheme
import net.techbrewery.tvphotoframe.core.ui.theme.TvTheme

@Composable
fun TvViewRoot(content: @Composable () -> Unit) {
    TvTheme {
        Surface(
            content = content
        )
    }
}

@Composable
fun MobileViewRoot(content: @Composable () -> Unit) {
    MobileTheme {
        Surface(
            content = content
        )
    }
}