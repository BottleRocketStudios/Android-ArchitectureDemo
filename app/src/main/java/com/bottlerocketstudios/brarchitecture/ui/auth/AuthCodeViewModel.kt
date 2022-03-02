package com.bottlerocketstudios.brarchitecture.ui.auth

import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AuthCodeViewModel (
    private val repo: BitbucketRepository,
    buildConfigProvider: BuildConfigProvider,
    private val toaster: Toaster,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel() {

    companion object {
        const val MIN_CODE_LENGTH = 6
    }

    //  Look at including this in BaseViewModel
    fun launchIO(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(dispatcherProvider.IO, block = block)

    // UI
    // TODO  - setup request URL and possibly link to browser on tap,
    //    Maybe look into displaying as QR code
    val requestUrl: StateFlow<String> = MutableStateFlow("")
    val entryCode: MutableStateFlow<String> = MutableStateFlow("")
    val loginEnabled = entryCode.map { it.length >= MIN_CODE_LENGTH }
    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild

    ///////////////////////////////////////////////////////////////////////////
    // Callbacks
    ///////////////////////////////////////////////////////////////////////////
    fun onLoginClicked() {

    }
}
