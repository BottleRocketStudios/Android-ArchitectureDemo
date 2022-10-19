package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.models.Branch
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.repository.RepositoryBranchItemUiModel
import com.bottlerocketstudios.compose.util.StringIdHelper
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject
import java.time.Clock

class RepositoryBranchesViewModel : BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()
    private val clock by inject<Clock>()

    // State
    private val srcBranches = MutableStateFlow<List<Branch>>(emptyList())
    val currentRepoName = MutableStateFlow("")

    // Internal state
    val repos = repo.repos.groundState(emptyList())

    init {
        launchIO {
            currentRepoName.collect { repoName ->
                getRepoBranches(repoName)
            }
        }
    }

    // UI
    val path: StateFlow<String> = MutableStateFlow("")
    val itemCount: StateFlow<Int> = srcBranches
        .map { it.size }
        .groundState(0)
    val uiModels: StateFlow<List<RepositoryBranchItemUiModel>> = srcBranches
        .map { branches ->
            branches.map { branch ->
                RepositoryBranchItemUiModel(
                    name = branch.name,
                    timeSinceCreated = branch.date.formattedUpdateTime(clock),
                    status = StringIdHelper.Id(R.string.home_branches_status_placeholder)
                )
            }
        }
        .groundState(emptyList())

    // Load Logic
    private fun getRepoBranches(name: String) {
        val selectedRepo = repos.value.firstOrNull { it.name?.equals(name) ?: false }
        path.setValue(name)
        selectedRepo?.let {
            val slug = it.workspace?.slug ?: ""
            val repoName = it.name ?: ""
            launchIO {
                repo.getBranches(slug, repoName).handlingErrors(R.string.error_loading_branches) { branchCallResult: List<Branch> ->
                    srcBranches.value = branchCallResult
                }
            }
        }
    }
}
