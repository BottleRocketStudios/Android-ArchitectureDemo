package com.bottlerocketstudios.brarchitecture.ui.auth

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.model.CredentialModel
import com.bottlerocketstudios.brarchitecture.data.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(app: Application, private val repo: BitbucketRepository, private val dispatcherProvider: DispatcherProvider) : BaseViewModel(app) {
    val textWatcher = Observer<String> { _ ->
        _loginEnabled.postValue(CredentialModel(email.value, password.value).valid)
    }

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled: LiveData<Boolean> = _loginEnabled
    private val _authenticated = MutableLiveData<Boolean>()
    val authenticated: LiveData<Boolean> = _authenticated

    init {
        _loginEnabled.postValue(false)
        email.observeForever(textWatcher)
        password.observeForever(textWatcher)
    }

    fun onLoginClicked() {
        val creds = CredentialModel(email.value, password.value)
        creds.validCredentials?.let {
            authenticate(it)
        }
    }

    private fun authenticate(creds: ValidCredentialModel) {
        viewModelScope.launch(dispatcherProvider.IO) {
            _authenticated.postValue(repo.authenticate(creds))
        }
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
