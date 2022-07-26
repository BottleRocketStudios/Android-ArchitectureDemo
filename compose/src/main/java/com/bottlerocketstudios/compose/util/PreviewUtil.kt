package com.bottlerocketstudios.compose.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme

@Composable
fun Preview(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) = ArchitectureDemoTheme(
    darkTheme = darkTheme,
    content = content,
)

@SuppressLint("UnrememberedMutableState")
@Composable
fun <T> T.asMutableState() = mutableStateOf(this)

@Preview(showSystemUi = true, device = Devices.FOLDABLE)
@Preview(showSystemUi = true, device = Devices.TABLET)
@Preview(showSystemUi = true, device = Devices.DEFAULT)
annotation class PreviewAllDevices
