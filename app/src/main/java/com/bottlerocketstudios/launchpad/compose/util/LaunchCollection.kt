package com.bottlerocketstudios.launchpad.compose.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

@SuppressLint("ComposableNaming")
@Composable
fun <T> Flow<T>.LaunchCollection(collector: suspend (T) -> Unit) {
    LaunchedEffect(this) {
        collect(collector)
    }
}
