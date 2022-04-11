package com.bottlerocketstudios.brarchitecture.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.bottlerocketstudios.brarchitecture.ui.popToMainInclusive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

fun NavController.navigateAsTopLevel(route: String) = navigate(route) { popToMainInclusive() }

@Composable
fun <T> Flow<T>.LaunchCollection(collector: FlowCollector<T>) =
    LaunchedEffect(key1 = this) { collect(collector) }
