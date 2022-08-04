package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.CommitDto
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.infrastructure.util.exhaustive
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.repository.RepositoryCommitItemUiModel
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject
import java.time.Clock

class RepositoryCommitViewModel : BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()
    private val clock by inject<Clock>()

    companion object {
        const val HASH_LENGTH = 6
    }

    // State
    private val srcCommits = MutableStateFlow<List<CommitDto>>(emptyList())
    var currentRepoName = MutableStateFlow("")

    init {
        launchIO {
            currentRepoName.collect {
                repoName ->
                getRepoCommits(repoName)
            }
        }
    }

    // UI
    val path: StateFlow<String> = MutableStateFlow("")
    val itemCount: StateFlow<Int> = srcCommits
        .map { it.size }
        .groundState(0)
    val uiModels: StateFlow<List<RepositoryCommitItemUiModel>> = srcCommits
        .map { commits ->
            commits.map { commit ->
                RepositoryCommitItemUiModel(
                    author = commit.author?.userInfo?.displayName ?: "",
                    timeSinceCommitted = commit.date.formattedUpdateTime(clock),
                    hash = commit.hash?.take(HASH_LENGTH) ?: "",
                    message = commit.message ?: ""
                )
            }
        }
        .groundState(emptyList())

    // Events

    // Load Logic
    private fun getRepoCommits(name: String) {
        val selectedRepo = repo.repos.value.firstOrNull { it.name?.equals(name) ?: false }
        path.setValue(name)
        selectedRepo?.let {
            val slug = it.workspaceDto?.slug ?: ""
            val repoName = it.name ?: ""
            launchIO {
                val result = repo.getCommits(slug, repoName)
                when (result) {
                    is Status.Success -> srcCommits.value = result.data
                    is Status.Failure -> handleError(R.string.error_loading_commits)
                }.exhaustive
            }
        }
    }
}
