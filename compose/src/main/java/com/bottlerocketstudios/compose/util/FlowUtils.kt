package com.bottlerocketstudios.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

@Composable
fun <T> Flow<T>.LaunchCollection(collector: FlowCollector<T>) =
    LaunchedEffect(key1 = this) { collect(collector) }
