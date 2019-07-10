package com.bottlerocketstudios.brarchitecture.ui.user

import android.app.Application
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.RepoViewModel

class UserFragmentViewModel(app: Application, repo: BitbucketRepository) : RepoViewModel(app, repo) {
    override fun onCleared() {
        super.onCleared()
        doClear()
    }

    fun doClear() {
    }
}
