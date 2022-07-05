package com.bottlerocketstudios.compose.widgets.listdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableSharedFlow

@Immutable
interface ListDetailScope<T> {
    val list: @Composable (List<T>, (T) -> Unit) -> Unit
    val detail: @Composable (T?) -> Unit
    val detailStateCallback: (Boolean) -> Unit
    val selector: MutableSharedFlow<String?>

    @Composable
    fun List(newList: @Composable (List<T>, (T) -> Unit) -> Unit)

    @Composable
    fun Detail(newDetail: @Composable (T?) -> Unit)

    @Composable
    fun DetailState(newDetailState: (Boolean) -> Unit)

    suspend fun select(key: String?)
}

internal class ListDetailScopeImpl<T>(
    val items: List<T>
) : ListDetailScope<T> {
    override var list: @Composable (List<T>, (T) -> Unit) -> Unit = { _, _ -> }
        private set

    override var detail: @Composable (T?) -> Unit = {}
        private set

    override var detailStateCallback: (Boolean) -> Unit = {}
        private set

    override val selector = MutableSharedFlow<String?>()

    @Composable
    override fun List(newList: @Composable (List<T>, (T) -> Unit) -> Unit) {
        list = newList
    }

    @Composable
    override fun Detail(newDetail: @Composable (T?) -> Unit) {
        detail = newDetail
    }

    @Composable
    override fun DetailState(newDetailState:  (Boolean) -> Unit) {
        detailStateCallback = newDetailState
    }

    override suspend fun select(key: String?) {
        selector.emit(key)
    }
}
