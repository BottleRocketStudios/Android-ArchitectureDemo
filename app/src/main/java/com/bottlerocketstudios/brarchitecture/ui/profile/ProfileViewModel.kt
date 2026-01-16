package com.bottlerocketstudios.brarchitecture.ui.profile

import android.content.Intent
import androidx.core.net.toUri
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.repositories.CognitoRepository
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.brarchitecture.domain.models.CognitoUser
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject

class ProfileViewModel : BaseViewModel() {
    // region DI
    private val repo: BitbucketRepository by inject()
    private val cognitoRepo: CognitoRepository by inject()

    private val bitbucketUser = repo.user
    private val cognitoUser = MutableStateFlow<CognitoUser?>(null)
    // endregion

    init {
        launchIO {
            cognitoUser.value = cognitoRepo.getUser()
        }
    }

    // region UI State
    val avatarUrl: StateFlow<String> = bitbucketUser.map { it?.avatarUrl.orEmpty() }.groundState("")
    val displayName: Flow<String> = combine(bitbucketUser, cognitoUser) { bitbucket, cognito ->
        bitbucket?.displayName ?: cognito?.name ?: ""
    }
    val nickname: Flow<String> = combine(bitbucketUser, cognitoUser) { bitbucket, cognito ->
        bitbucket?.nickname ?: cognito?.username ?: ""
    }
    // endregion

    // region Events
    val onLogout = MutableSharedFlow<Unit>()
    // endregion

    // region UI Callbacks
    fun onEditClicked() {
        externalNavigationEvent.postValue(
                ExternalNavigationEvent(Intent(Intent.ACTION_VIEW, BIT_BUCKET_SETTING_URL.toUri()))
        )
    }

    fun onLogoutClicked() {
        launchIO {
            repo.clear()
            cognitoRepo.clear()
            onLogout.emit(Unit)
        }
    }
    // endregion

    companion object {
        private const val BIT_BUCKET_SETTING_URL = "https://bitbucket.org/account/settings/"
    }
}
