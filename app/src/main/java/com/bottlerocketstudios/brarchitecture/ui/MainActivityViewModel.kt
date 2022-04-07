package com.bottlerocketstudios.brarchitecture.ui

import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.converter.convertToUser
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MainActivityViewModel(val repo: BitbucketRepository, buildConfigProvider: BuildConfigProvider) : BaseViewModel() {
    val selectedRepo = MutableStateFlow(GitRepository(null, null, null, null, null, null, null))
    val title = MutableStateFlow("")
    val showToolbar = title.map { it.isNotEmpty() }
    val topLevel = MutableStateFlow(false)

    val avatarUrl = repo.user.map { it?.convertToUser()?.avatarUrl.orEmpty() }.groundState("")
    val displayName = repo.user.map { it?.displayName.orEmpty() }.groundState("")
    val username = repo.user.map { it?.username.orEmpty() }.groundState("")

    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild
}
