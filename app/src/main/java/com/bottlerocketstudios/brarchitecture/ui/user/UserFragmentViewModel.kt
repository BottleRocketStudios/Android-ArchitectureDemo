package com.bottlerocketstudios.brarchitecture.ui.user

import android.app.Application
import android.content.Intent
import androidx.core.net.toUri
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel

class UserFragmentViewModel(app: Application, val repo: BitbucketRepository) : BaseViewModel(app) {

    fun onEditClicked() {
        externalNavigationEvent.postValue(ExternalNavigationEvent(Intent(Intent.ACTION_VIEW, "https://bitbucket.org/account/settings/".toUri())))
    }

    fun onLogoutClicked() {
        repo.clear()
        navigationEvent.postValue(NavigationEvent.Action(R.id.action_userFragment_to_loginFragment))
    }
}
