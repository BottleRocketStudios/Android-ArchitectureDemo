package com.bottlerocketstudios.compose.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme

@Composable
fun Preview(content: @Composable () -> Unit) = ArchitectureDemoTheme(content = content)

@SuppressLint("UnrememberedMutableState")
@Composable
fun <T> T.asMutableState() = mutableStateOf(this)
