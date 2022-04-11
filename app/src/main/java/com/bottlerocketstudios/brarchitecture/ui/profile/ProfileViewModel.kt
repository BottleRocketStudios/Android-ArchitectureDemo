package com.bottlerocketstudios.brarchitecture.ui.profile

import android.content.Intent
import androidx.core.net.toUri
import com.bottlerocketstudios.brarchitecture.data.converter.convertToUser
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileViewModel(val repo: BitbucketRepository) : BaseViewModel() {
    val avatarUrl: Flow<String> = repo.user.map { it?.convertToUser()?.avatarUrl.orEmpty() }
    val displayName: Flow<String> = repo.user.map { it?.displayName.orEmpty() }
    val nickname: Flow<String> = repo.user.map { it?.nickname.orEmpty() }

    fun onEditClicked() {
        externalNavigationEvent.postValue(ExternalNavigationEvent(Intent(Intent.ACTION_VIEW, BIT_BUCKET_SETTING_URL.toUri())))
    }

    fun onLogoutClicked() {
        repo.clear()
        // navigationEvent.postValue(NavigationEvent.Action(R.id.action_global_to_authCodeFragment))
    }

    companion object {
        private const val BIT_BUCKET_SETTING_URL = "https://bitbucket.org/account/settings/"
    }
}
