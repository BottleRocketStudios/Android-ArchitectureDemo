package com.bottlerocketstudios.brarchitecture.ui.auth

import android.app.Application
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LoginViewModel(
    private val app: Application,
    private val repo: BitbucketRepository,
    buildConfigProvider: BuildConfigProvider,
    private val toaster: Toaster,
    private val dispatcherProvider: DispatcherProvider
) :
    BaseViewModel(app) {
    val textWatcher = Observer<String> { _ ->
        loginEnabled.postValue(CredentialModel(email.value, password.value).valid)
    }

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginEnabled: LiveData<Boolean> = MutableLiveData()
    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild

    init {
        loginEnabled.postValue(false)
        email.observeForever(textWatcher)
        password.observeForever(textWatcher)
    }

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

    override fun onCleared() {
        super.onCleared()
        doClear()
    }

    fun doClear() {
        email.removeObserver(textWatcher)
        password.removeObserver(textWatcher)
    }
}
