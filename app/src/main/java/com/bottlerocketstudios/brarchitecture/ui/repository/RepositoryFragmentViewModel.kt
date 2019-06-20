package com.bottlerocketstudios.brarchitecture.ui.repository

import android.app.Application
import com.bottlerocketstudios.brarchitecture.ui.ScopedViewModel

class RepositoryFragmentViewModel(app: Application) : ScopedViewModel(app) {
    override fun onCleared() {
        super.onCleared()
        doClear()
    }

    fun doClear() {
    }
}
