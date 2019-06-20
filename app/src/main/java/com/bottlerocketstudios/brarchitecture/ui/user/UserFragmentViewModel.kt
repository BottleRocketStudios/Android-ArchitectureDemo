package com.bottlerocketstudios.brarchitecture.ui.user

import android.app.Application
import com.bottlerocketstudios.brarchitecture.ui.ScopedViewModel

class UserFragmentViewModel(app: Application) : ScopedViewModel(app) {
    override fun onCleared() {
        super.onCleared()
        doClear()
    }

    fun doClear() {
    }
}
