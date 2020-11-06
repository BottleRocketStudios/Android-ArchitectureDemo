package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository

class MainActivityViewModel(app: Application, val repo: BitbucketRepository, buildConfigProvider: BuildConfigProvider) : BaseViewModel(app) {
    private val _selectedRepo = MutableLiveData<Repository>()
    val selectedRepo: LiveData<Repository> = _selectedRepo
    private val _showToolbar = MutableLiveData<Boolean>()
    val showToolbar: LiveData<Boolean> = _showToolbar
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild

    fun selectRepo(repo: Repository) {
        _selectedRepo.postValue(repo)
    }

    fun showToolbar(show: Boolean) {
        _showToolbar.postValue(show)
    }

    fun setTitle(title: String) {
        _title.postValue(title)
    }
}
