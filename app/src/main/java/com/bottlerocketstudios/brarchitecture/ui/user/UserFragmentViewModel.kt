package com.bottlerocketstudios.brarchitecture.ui.user

import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.asLiveData
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.converter.convertToUser
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.map

class UserFragmentViewModel(val repo: BitbucketRepository) : BaseViewModel() {

    // TODO: Remove conversion from Flow to LiveData once it is in the next stable 7.x AGP: https://developer.android.com/topic/libraries/data-binding/observability#stateflow
    val avatarUrl = repo.user.map { it?.convertToUser()?.avatarUrl.orEmpty() }.asLiveData()
    val displayName = repo.user.map { it?.displayName.orEmpty() }.asLiveData()
    val nickname = repo.user.map { it?.nickname.orEmpty() }.asLiveData()

    fun onEditClicked() {
        externalNavigationEvent.postValue(ExternalNavigationEvent(Intent(Intent.ACTION_VIEW, "https://bitbucket.org/account/settings/".toUri())))
    }

    fun onLogoutClicked() {
        repo.clear()
        // Back Nav to auth code entry, if in stack.
        navigationEvent.postValue(NavigationEvent.Back(R.id.authCodeFragment))
    }
}
