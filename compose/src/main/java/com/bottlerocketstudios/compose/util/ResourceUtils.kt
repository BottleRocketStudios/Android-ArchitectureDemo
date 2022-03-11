package com.bottlerocketstudios.compose.util

import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

@Composable
@ReadOnlyComposable
fun pluralResource(
    @PluralsRes id: Int,
    quantity: Int,
    vararg formatArgs: Any? = emptyArray()
): String {
    val resources = resources()
    return resources.getQuantityString(id, quantity, *formatArgs)
}

/**
 * Stolen directly from the StringResources file provided by Compose. Returns the resources instance for
 * the current composable context and and references configuration so that it will be recomposed if
 * configuration changes.
 */
@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
