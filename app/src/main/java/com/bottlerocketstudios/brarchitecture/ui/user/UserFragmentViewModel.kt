package com.bottlerocketstudios.brarchitecture.ui.user

import android.app.Application
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.asLiveData
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.map

class UserFragmentViewModel(app: Application, val repo: BitbucketRepository) : BaseViewModel(app) {

    // TODO: Remove conversion from Flow to LiveData once it is in the next stable 7.x AGP: https://developer.android.com/topic/libraries/data-binding/observability#stateflow
    val avatarUrl = repo.user.map { it?.avatarUrl.orEmpty() }.asLiveData()
    val displayName = repo.user.map { it?.displayName.orEmpty() }.asLiveData()
    val nickname = repo.user.map { it?.nickname.orEmpty() }.asLiveData()

    fun onEditClicked() {
        externalNavigationEvent.postValue(ExternalNavigationEvent(Intent(Intent.ACTION_VIEW, "https://bitbucket.org/account/settings/".toUri())))
    }

    fun onLogoutClicked() {
        repo.clear()
        navigationEvent.postValue(NavigationEvent.Action(R.id.action_userFragment_to_loginFragment))
    }
}
