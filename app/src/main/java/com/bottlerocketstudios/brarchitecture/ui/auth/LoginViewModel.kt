package com.bottlerocketstudios.brarchitecture.ui.auth

import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.model.CredentialModel
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LoginViewModel(
    private val repo: BitbucketRepository,
    buildConfigProvider: BuildConfigProvider,
    private val toaster: Toaster,
    private val dispatcherProvider: DispatcherProvider
) :
    BaseViewModel() {

    // Two way databinding
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    // One way databinding
    val loginEnabled: StateFlow<Boolean> = combine(email, password) { (email, password) ->
        CredentialModel(email, password).valid
    }.groundState(false)
    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild

    fun onLoginClicked() {
        Timber.v("[onLoginClicked]")
        viewModelScope.launch(dispatcherProvider.IO) {
            val creds = CredentialModel(email.value, password.value)
            creds.validCredentials?.let {

                val authenticated = repo.authenticate(it)
                when {
                    // TODO: Improve error messaging (update text inputs, show dialog or snackbar, etc)
                    !authenticated -> {
                        withContext(dispatcherProvider.Main) {
                            toaster.toast(R.string.login_error)
                        }
                    }
                    else -> navigationEvent.postValue(NavigationEvent.Action(R.id.action_loginFragment_to_homeFragment))
                }
            } ?: run {
                // TODO: Need to represent invalid credential format error here to differentiate from an actual invalid login attempt
                withContext(dispatcherProvider.Main) {
                    toaster.toast(R.string.login_error)
                }
            }
        }
    }

    fun onSignupClicked() {
        externalNavigationEvent.postValue(ExternalNavigationEvent(Intent(Intent.ACTION_VIEW, "https://id.atlassian.com/signup?application=bitbucket".toUri())))
    }

    fun onForgotClicked() {
        externalNavigationEvent.postValue(ExternalNavigationEvent(Intent(Intent.ACTION_VIEW, "https://id.atlassian.com/login/resetpassword?application=bitbucket".toUri())))
    }

    fun onDevOptionsClicked() {
        navigationEvent.postValue(NavigationEvent.Action(R.id.action_loginFragment_to_devOptionsFragment))
    }
}
