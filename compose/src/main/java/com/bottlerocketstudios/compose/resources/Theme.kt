package com.bottlerocketstudios.compose.resources

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun ProvideColors(
    colors: Colors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { colors }
    CompositionLocalProvider(LocalAppColors provides colorPalette, content = content)
}

// TODO update this to use compositionLocalOf when a set of dark mode colors has been added
private val LocalAppColors = staticCompositionLocalOf {
    lightColors
}

@Composable
fun ProvideDimens(
    dimensions: Dimensions,
    content: @Composable () -> Unit
) {
    val dimensionSet = remember { dimensions }
    CompositionLocalProvider(LocalAppDimens provides dimensionSet, content = content)
}

private val LocalAppDimens = compositionLocalOf {
    sw360Dimensions
}

@Composable
fun ArchitectureDemoTheme(
    content: @Composable () -> Unit
) {
    // TODO This should be updated later to support a dark mode check
    val colors = lightColors
    val configuration = LocalConfiguration.current
    val dimensions = if (configuration.screenWidthDp <= 360) smallDimensions else sw360Dimensions

    ProvideDimens(dimensions = dimensions) {
        ProvideColors(colors = colors) {
            MaterialTheme(
                colors = lightColors,
                typography = typography
            ) {
                content()
            }
        }
    }
}

object ArchitectureDemoTheme {
    val colors: Colors
        @Composable
        get() = LocalAppColors.current

    val dimens: Dimensions
        @Composable
        get() = LocalAppDimens.current
}

val Dimens: Dimensions
    @Composable
    get() = ArchitectureDemoTheme.dimens