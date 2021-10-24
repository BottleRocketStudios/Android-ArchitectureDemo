package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainActivityViewModel(app: Application, val repo: BitbucketRepository, buildConfigProvider: BuildConfigProvider) : BaseViewModel(app) {
    val selectedRepo: StateFlow<Repository> = MutableStateFlow(Repository())
    val showToolbar: StateFlow<Boolean> = MutableStateFlow(false)
    val title: StateFlow<String> = MutableStateFlow("")

    val avatarUrl = repo.user.map { it?.avatarUrl.orEmpty() }.stateIn(viewModelScope, SharingStarted.Lazily, "")
    val displayName = repo.user.map { it?.displayName.orEmpty() }.stateIn(viewModelScope, SharingStarted.Lazily, "")
    val username = repo.user.map { it?.username.orEmpty() }.stateIn(viewModelScope, SharingStarted.Lazily, "")

    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild

    fun selectRepo(repo: Repository) = selectedRepo.set(repo)

    fun showToolbar(show: Boolean) = showToolbar.setNullable(show)

    fun setTitle(title: String) = this.title.set(title)
}
