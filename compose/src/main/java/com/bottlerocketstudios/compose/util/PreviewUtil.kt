package com.bottlerocketstudios.compose.util

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme

@Composable
fun Preview(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) = ArchitectureDemoTheme(
    darkTheme = darkTheme,
    content = content,
)

@SuppressLint("UnrememberedMutableState")
fun <T> T.asMutableState() = mutableStateOf(this)

@Preview(showSystemUi = true, device = Devices.FOLDABLE, group = "Foldable")
@Preview(showSystemUi = true, device = Devices.TABLET, group = "Tablet")
@PreviewLightDark
annotation class PreviewAll

@Preview(showSystemUi = true, device = Devices.DEFAULT, group = "Default")
@Preview(showSystemUi = true, device = Devices.DEFAULT, group = "Default", uiMode = UI_MODE_NIGHT_YES)
annotation class PreviewLightDark

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
annotation class PreviewComposable
