package com.bottlerocketstudios.brarchitecture.ui.profile

import android.content.Intent
import androidx.core.net.toUri
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject

class ProfileViewModel : BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()

    // UI
    val avatarUrl: StateFlow<String> = repo.user.map { it?.avatarUrl.orEmpty() }.groundState("")
    val displayName: Flow<String> = repo.user.map { it?.displayName.orEmpty() }
    val nickname: Flow<String> = repo.user.map { it?.nickname.orEmpty() }

    // Events
    val onLogout = MutableSharedFlow<Unit>()

    // UI Callbacks
    fun onEditClicked() {
        externalNavigationEvent.postValue(ExternalNavigationEvent(Intent(Intent.ACTION_VIEW, BIT_BUCKET_SETTING_URL.toUri())))
    }

    fun onLogoutClicked() {
        repo.clear()
        launchIO {
            onLogout.emit(Unit)
        }
    }

    companion object {
        private const val BIT_BUCKET_SETTING_URL = "https://bitbucket.org/account/settings/"
    }
}
