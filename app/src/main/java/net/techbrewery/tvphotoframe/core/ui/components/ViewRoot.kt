package net.techbrewery.tvphotoframe.core.ui.components

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import net.techbrewery.tvphotoframe.core.ui.theme.AppTheme

@Composable
fun ViewRoot(content: @Composable () -> Unit) {
    AppTheme {
        Surface(
            content = content
        )
    }
}