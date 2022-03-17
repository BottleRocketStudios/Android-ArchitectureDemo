package com.bottlerocketstudios.brarchitecture.ui

import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.converter.convertToUser
import com.bottlerocketstudios.brarchitecture.data.model.RepositoryDto
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class MainActivityViewModel(val repo: BitbucketRepository, buildConfigProvider: BuildConfigProvider) : BaseViewModel() {
    val selectedRepo: StateFlow<RepositoryDto> = MutableStateFlow(RepositoryDto())
    val showToolbar: StateFlow<Boolean> = MutableStateFlow(false)
    val title: StateFlow<String> = MutableStateFlow("")

    val avatarUrl = repo.user.map { it?.convertToUser()?.avatarUrl.orEmpty() }.groundState("")
    val displayName = repo.user.map { it?.displayName.orEmpty() }.groundState("")
    val username = repo.user.map { it?.username.orEmpty() }.groundState("")

    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild

    fun selectRepo(repo: Repository) = selectedRepo.set(repo)
    fun showToolbar(show: Boolean) = showToolbar.set(show)
    fun setTitle(title: String) = this.title.set(title)
}
