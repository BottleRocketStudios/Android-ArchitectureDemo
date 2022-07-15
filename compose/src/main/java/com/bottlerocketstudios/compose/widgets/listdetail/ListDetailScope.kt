package com.bottlerocketstudios.compose.widgets.listdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * List detail scope - This scope is used by [AnimatedListDetail] to pass in definitions for List and Detail Composable UI
 * **Note** This follows same pattern as [LazyListScope]
 *
 * @param T - Type of item used in list and detail
 */
@Immutable
interface ListDetailScope<T> {
    val list: @Composable (List<T>, (T) -> Unit) -> Unit
    val detail: @Composable (T?) -> Unit
    val detailStateCallback: (Boolean) -> Unit
    val selector: MutableSharedFlow<String?>

    /**
     * List - DSL function for defining list UI
     *
     * @param newList - Code block that accepts list of [T] and selected [T] to build list UI
     */
    @Composable
    fun List(newList: @Composable (List<T>, (T) -> Unit) -> Unit)

    /**
     * Detail - DSL function for defining detail UI
     *
     * @param newDetail - Code block that accepts selected [T] and builds detail UI
     */
    @Composable
    fun Detail(newDetail: @Composable (T?) -> Unit)

    /**
     * Detail state - DSL function for defining state change callback
     *
     * @param newDetailState - Callback that is used to notify parent scope that an item has been selected or not.
     */
    @Composable
    fun DetailState(newDetailState: (Boolean) -> Unit)

    /**
     * Select - Public function for selecting [T]
     *
     * @param - Key used by [AnimatedListDetail] to identify [T]
     */
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
    override fun DetailState(newDetailState: (Boolean) -> Unit) {
        detailStateCallback = newDetailState
    }

    override suspend fun select(key: String?) {
        selector.emit(key)
    }
}
