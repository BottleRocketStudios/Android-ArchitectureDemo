package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository


class MainActivityViewModel(app: Application, repo: BitbucketRepository) : RepoViewModel(app, repo) {
    val selectedRepo = MutableLiveData<Repository>()
}
