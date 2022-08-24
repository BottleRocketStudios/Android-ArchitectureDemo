package com.bottlerocketstudios.brarchitecture.ui

import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject

class ComposeActivityViewModel : BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()
    private val buildConfigProvider: BuildConfigProvider by inject()

    // UI
    val title = MutableStateFlow("")
    val showToolbar = title.map { it.isNotEmpty() }
    val topLevel = MutableStateFlow(false)

    // State
    val selectedRepo = MutableStateFlow(GitRepository(null, null, null, null, null, null, null))
    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild

    // Profile info
    val avatarUrl = repo.user.map { it?.avatarUrl.orEmpty() }.groundState("")
    val displayName = repo.user.map { it?.displayName.orEmpty() }.groundState("")
    val username = repo.user.map { it?.username.orEmpty() }.groundState("")
}
