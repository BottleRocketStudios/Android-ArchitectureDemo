package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository

class MainActivityViewModel(app: Application, val repo: BitbucketRepository, buildConfigProvider: BuildConfigProvider) : BaseViewModel(app) {
    val selectedRepo: LiveData<Repository> = MutableLiveData()
    val showToolbar: LiveData<Boolean> = MutableLiveData()
    val title: LiveData<String> = MutableLiveData()

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
