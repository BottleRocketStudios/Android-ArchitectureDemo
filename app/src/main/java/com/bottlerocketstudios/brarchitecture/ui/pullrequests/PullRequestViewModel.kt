package com.bottlerocketstudios.brarchitecture.ui.pullrequests

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.pullrequest.PullRequestItemState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class PullRequestViewModel : BaseViewModel() {

    // DI
    private val repo: BitbucketRepository by inject()

    val pullRequestRequestList = MutableStateFlow<List<PullRequestItemState>>(emptyList())

    // Init logic
    init {
        viewModelScope.launch(dispatcherProvider.IO) {
            repo.getPullRequests("ddeleon92")
        }
    }
}
