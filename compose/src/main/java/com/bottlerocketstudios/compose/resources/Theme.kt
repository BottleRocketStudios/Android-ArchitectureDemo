package com.bottlerocketstudios.compose.resources

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun ProvideColors(
    colors: ArchitectureDemoColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { colors }
    CompositionLocalProvider(LocalAppColors provides colorPalette, content = content)
}

private val LocalAppColors = staticCompositionLocalOf {
    lightColors
}
private const val SMALL_SCREEN_WIDTH_DP = 360

@Composable
fun ProvideDimens(
    dimensions: Dimensions,
    content: @Composable () -> Unit
) {
    val dimensionSet = remember { dimensions }
    CompositionLocalProvider(LocalAppDimens provides dimensionSet, content = content)
}

private val LocalAppDimens = staticCompositionLocalOf {
    sw360Dimensions
}

@Composable
fun ArchitectureDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // TODO This should be updated later to support a dark mode check
    val colors = if (darkTheme) darkColors else lightColors
    val configuration = LocalConfiguration.current
    val dimensions = if (configuration.screenWidthDp <= SMALL_SCREEN_WIDTH_DP) smallDimensions else sw360Dimensions

    ProvideDimens(dimensions = dimensions) {
        ProvideColors(colors = colors) {
            MaterialTheme(
                colors = colors.materialColors,
                typography = typography
            ) {
                content()
            }
        }
    }
}

object ArchitectureDemoTheme {
    val colors: ArchitectureDemoColors
        @Composable
        get() = LocalAppColors.current

    val dimens: Dimensions
        @Composable
        get() = LocalAppDimens.current
}

val Dimens: Dimensions
    @Composable
    get() = ArchitectureDemoTheme.dimens

val Colors: ArchitectureDemoColors
    @Composable
    get() = ArchitectureDemoTheme.colors
