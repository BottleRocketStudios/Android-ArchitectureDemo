package com.bottlerocketstudios.brarchitecture.ui

import androidx.compose.runtime.MutableState
import com.bottlerocketstudios.brarchitecture.domain.utils.MutableStateFlowDelegate
import com.bottlerocketstudios.compose.util.MutableStateDelegate

interface MainWindowControls {
    var title: String
    var topLevel: Boolean
    var navIntercept: (() -> Boolean)?

    fun reset()
    companion object {
        const val EMPTY_TOOLBAR_TITLE = " "
    }
}

class MainWindowControlsImplementation(
    viewModel: ComposeActivityViewModel,
    navIntercept: MutableState<(() -> Boolean)?>
) : MainWindowControls {
    override var title by MutableStateFlowDelegate(viewModel.title)
    override var topLevel by MutableStateFlowDelegate(viewModel.topLevel)
    override var navIntercept by MutableStateDelegate(navIntercept)

    override fun reset() {
        title = ""
        topLevel = false
    }

}
