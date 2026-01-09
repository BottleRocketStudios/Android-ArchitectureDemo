package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.models.Branch
import com.bottlerocketstudios.brarchitecture.domain.models.Commit
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.repository.RepositoryCommitItemUiModel
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import java.time.Clock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject

class RepositoryCommitViewModel : BaseViewModel() {
    // region DI
    private val repo: BitbucketRepository by inject()
    private val clock by inject<Clock>()
    // endregion

    companion object {
        const val HASH_LENGTH = 6
    }

    // region UI State
    private val srcCommits = MutableStateFlow<List<CommitWithBranch>>(emptyList())
    private val branchNames = MutableStateFlow<List<String>>(emptyList())
    val currentRepoName = MutableStateFlow("")

    // Internal state
    val repos = repo.repos.groundState(emptyList())

    // region Init
    init {
        launchIO { currentRepoName.collect { repoName -> getRepoCommits(repoName) } }
    }
    // endregion

    // region UI State (continued)
    val path: StateFlow<String> = MutableStateFlow("")
    val itemCount: StateFlow<Int> = srcCommits.map { it.size }.groundState(0)
    val uiModels: StateFlow<List<RepositoryCommitItemUiModel>> =
            srcCommits
                    .map { commits ->
                        commits.map { commit ->
                            RepositoryCommitItemUiModel(
                                    author = commit.commit.author?.user?.displayName.orEmpty(),
                                    timeSinceCommitted =
                                            commit.commit.date.formattedUpdateTime(clock),
                                    hash = commit.commit.hash.take(HASH_LENGTH),
                                    message = commit.commit.message,
                                    branchName = commit.branchName,
                            )
                        }
                    }
                    .groundState(emptyList())
    val branchItems: StateFlow<List<String>> = branchNames
    // endregion

    // Events

    // region Helpers
    private fun getRepoCommits(name: String) {
        val selectedRepo = repos.value.firstOrNull { it.name?.equals(name) ?: false }
        val commitList = mutableListOf<CommitWithBranch>()
        path.setValue(name)
        selectedRepo?.let {
            val slug = it.workspace?.slug ?: ""
            val repoName = it.name ?: ""
            launchIO {
                repo.getBranches(slug, repoName).handlingErrors(R.string.error_loading_branches) {
                        branchCallResult: List<Branch> ->
                    branchNames.value =
                            branchCallResult.map { branch -> branch.name }.filter { branch ->
                                branch.isNotBlank()
                            } // only care about non-null/non-blank branches
                    branchNames.value.forEach { branch ->
                        repo.getCommits(slug, repoName, branch).handlingErrors(
                                        R.string.error_loading_commits
                                ) { commitCallResult ->
                            commitList.addAll(
                                    commitCallResult.map { commit ->
                                        CommitWithBranch(commit, branch)
                                    }
                            )
                        }
                    }
                }
                srcCommits.value = commitList
            }
        }
    }
    // endregion

    /** Used in place of Pair to provide additional context to the properties. */
    private data class CommitWithBranch(
            val commit: Commit,
            val branchName: String,
    )
}
