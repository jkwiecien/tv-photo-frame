package net.techbrewery.tvphotoframe.core.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


object DimensMobile {
    const val GRID_SIZE = 8
}

object SpacingMobile {

    @Stable
    val Tiny = (DimensMobile.GRID_SIZE / 2).dp

    @Stable
    val Small = (DimensMobile.GRID_SIZE * 1).dp

    @Stable
    val Medium = (DimensMobile.GRID_SIZE * 2).dp

    @Stable
    val Large = (DimensMobile.GRID_SIZE * 3).dp

    @Stable
    val XL = (DimensMobile.GRID_SIZE * 4).dp

    @Stable
    val XXL = (DimensMobile.GRID_SIZE * 5).dp

    @Composable
    fun TinySpacingBox() = Box(modifier = Modifier.size(Tiny))

    @Composable
    fun SmallSpacingBox() = Box(modifier = Modifier.size(Small))

    @Composable
    fun MediumSpacingBox() = Box(modifier = Modifier.size(Medium))

    @Composable
    fun LargeSpacingBox() = Box(modifier = Modifier.size(Large))

    @Composable
    fun XLargeSpacingBox() = Box(modifier = Modifier.size(XL))

    @Composable
    fun XXLargeSpacingBox() = Box(modifier = Modifier.size(XXL))
}