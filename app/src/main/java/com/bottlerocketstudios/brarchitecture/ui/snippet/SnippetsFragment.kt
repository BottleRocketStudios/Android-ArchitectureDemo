package com.bottlerocketstudios.brarchitecture.ui.snippet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.util.formattedUpdateTime
import com.bottlerocketstudios.compose.snippets.SnippetUiModel
import com.bottlerocketstudios.compose.snippets.SnippetsBrowserScreen
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Clock

class SnippetsFragment : BaseFragment<SnippetsFragmentViewModel>() {
    private val clock by inject<Clock>()
    override val fragmentViewModel: SnippetsFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        ComposeScreen {
            val state = fragmentViewModel.toState()
            setUpdatedOnString(state.snippets.value, clock)
            SnippetsBrowserScreen(state = state)
        }

    private fun setUpdatedOnString(snippetUiModelList: List<SnippetUiModel>, clock: Clock) {
        snippetUiModelList.forEach { snippetUiModel ->
            context?.let {
                snippetUiModel.updatedTimeString = snippetUiModel.updatedTime.formattedUpdateTime(clock = clock).getString(it)
            }
        }
    }
}
