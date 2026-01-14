package com.bottlerocketstudios.launchpad.compose.widgets.listdetail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

interface ListDetailScope<T> {
    fun list(content: @Composable (List<T>) -> Unit)

    fun detail(content: @Composable (T?) -> Unit)

    fun detailState(block: (Boolean) -> Unit)

    fun select(key: Any?)
}

@Composable
fun <T> AnimatedListDetail(
    list: List<T>,
    keyProvider: (T) -> Any,
    compactWidth: Boolean,
    scope: ListDetailScope<T>.() -> Unit
) {
    val selectedKey = remember { mutableStateOf<Any?>(null) }
    val scopeImpl = remember { ListDetailScopeImpl<T>(selectedKey) }

    // Execute the scope block to populate content lambdas
    scopeImpl.scope()

    val selectedItem = list.find { keyProvider(it) == selectedKey.value }
    val detailShowing = selectedKey.value != null

    SideEffect {
        scopeImpl.detailStateCallback?.invoke(detailShowing)
    }

    if (compactWidth) {
        Crossfade(targetState = detailShowing) { isDetail ->
            if (isDetail) {
                scopeImpl.detailContent(selectedItem)
            } else {
                scopeImpl.listContent(list)
            }
        }
    } else {
        Row(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f).fillMaxHeight()) {
                scopeImpl.listContent(list)
            }
            Box(Modifier.weight(1f).fillMaxHeight()) {
                scopeImpl.detailContent(selectedItem)
            }
        }
    }
}

internal class ListDetailScopeImpl<T>(
    val selectedKey: MutableState<Any?>
) : ListDetailScope<T> {
    var listContent: @Composable (List<T>) -> Unit = {}
    var detailContent: @Composable (T?) -> Unit = {}
    var detailStateCallback: ((Boolean) -> Unit)? = null

    override fun list(content: @Composable (List<T>) -> Unit) {
        listContent = content
    }

    override fun detail(content: @Composable (T?) -> Unit) {
        detailContent = content
    }

    override fun detailState(block: (Boolean) -> Unit) {
        detailStateCallback = block
    }

    override fun select(key: Any?) {
        selectedKey.value = key
    }
}
