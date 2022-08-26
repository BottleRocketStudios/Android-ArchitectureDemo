package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.CommitDto
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
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
    private val srcCommits = MutableStateFlow<List<CommitDtoWithBranch>>(emptyList())
    private val branchNames = MutableStateFlow<List<String>>(emptyList())
    val currentRepoName = MutableStateFlow("")

    init {
        launchIO {
            currentRepoName.collect { repoName ->
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
                    author = commit.commitDto.author?.userInfo?.displayName ?: "",
                    timeSinceCommitted = commit.commitDto.date.formattedUpdateTime(clock),
                    hash = commit.commitDto.hash?.take(HASH_LENGTH) ?: "",
                    message = commit.commitDto.message ?: "",
                    branchName = commit.branchName,
                )
            }
        }
        .groundState(emptyList())
    val branchItems: StateFlow<List<String>> = branchNames

    // Events

    // Load Logic
    private fun getRepoCommits(name: String) {
        val selectedRepo = repo.repos.value.firstOrNull { it.name?.equals(name) ?: false }
        val commitList = mutableListOf<CommitDtoWithBranch>()
        path.setValue(name)
        selectedRepo?.let {
            val slug = it.workspaceDto?.slug ?: ""
            val repoName = it.name ?: ""
            launchIO {
                repo.getBranches(slug, repoName).handlingErrors(R.string.error_loading_commits) { branchCallResult ->
                    branchNames.value = branchCallResult.mapNotNull { branch -> branch.name }.filter { branch -> branch.isNotBlank() } // only care about non-null/non-blank branches
                    branchNames.value.forEach { branch ->
                        repo.getCommits(slug, repoName, branch).handlingErrors(R.string.error_loading_commits) { commitCallResult ->
                            commitList.addAll(commitCallResult.map { commitDto -> CommitDtoWithBranch(commitDto, branch) })
                        }
                    }
                }
                srcCommits.value = commitList
            }
        }
    }

    /** Used in place of Pair to provide additional context to the properties. */
    private data class CommitDtoWithBranch(
        val commitDto: CommitDto,
        val branchName: String,
    )
}
