package com.bottlerocketstudios.brarchitecture.ui.auth

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bottlerocketstudios.brarchitecture.domain.model.CredentialModel
import com.bottlerocketstudios.brarchitecture.domain.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.RepoViewModel
import kotlinx.coroutines.launch

class LoginViewModel(app: Application, repo: BitbucketRepository) : RepoViewModel(app, repo) {
    val textWatcher = Observer<String> { _ ->
        loginEnabled.postValue(CredentialModel(email.value, password.value).valid)
    }

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    private val loginEnabled = MutableLiveData<Boolean>()
    fun getLoginEnabled(): LiveData<Boolean> = loginEnabled
    private val authenticated = MutableLiveData<Boolean>()
    fun getAuthenticated(): LiveData<Boolean> = authenticated

    init {
        loginEnabled.postValue(false)
        email.observeForever(textWatcher)
        password.observeForever(textWatcher)
    }

    fun onLoginClicked(v: View) {
        val creds = CredentialModel(email.value, password.value)
        creds.validCredentials?.let {
            authenticate(it)
        }
    }

    private fun authenticate(creds: ValidCredentialModel) {
        launch {
            authenticated.postValue(repo.authenticate(creds))
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