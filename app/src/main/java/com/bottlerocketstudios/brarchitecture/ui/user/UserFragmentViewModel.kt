package com.bottlerocketstudios.brarchitecture.ui.user

import android.app.Application
import androidx.lifecycle.LiveData
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.hadilq.liveevent.LiveEvent

class UserFragmentViewModel(app: Application, val repo: BitbucketRepository) : BaseViewModel(app) {
    private val _editClicked = LiveEvent<Unit>()
    val editClicked: LiveData<Unit> = _editClicked
    private val _logoutClicked = LiveEvent<Unit>()
    val logoutClicked: LiveData<Unit> = _logoutClicked

    fun onEditClicked() {
        _editClicked.postValue(Unit)
    }
    
    fun onLogoutClicked() {
        repo.clear()
        _logoutClicked.postValue(Unit)
    }
}
