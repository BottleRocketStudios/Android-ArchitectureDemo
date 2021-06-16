package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import kotlinx.coroutines.flow.map

class MainActivityViewModel(app: Application, val repo: BitbucketRepository, buildConfigProvider: BuildConfigProvider) : BaseViewModel(app) {
    val selectedRepo: LiveData<Repository> = MutableLiveData()
    val showToolbar: LiveData<Boolean> = MutableLiveData()
    val title: LiveData<String> = MutableLiveData()

    // TODO: Remove conversion from Flow to LiveData once it is in the next stable 7.x AGP: https://developer.android.com/topic/libraries/data-binding/observability#stateflow
    val avatarUrl = repo.user.map { it?.avatarUrl.orEmpty() }.asLiveData()
    val displayName = repo.user.map { it?.displayName.orEmpty() }.asLiveData()
    val username = repo.user.map { it?.username.orEmpty() }.asLiveData()

    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild

    fun selectRepo(repo: Repository) {
        selectedRepo.postValue(repo)
    }

    fun showToolbar(show: Boolean) {
        showToolbar.postValue(show)
    }

    fun setTitle(title: String) {
        this.title.postValue(title)
    }
}
