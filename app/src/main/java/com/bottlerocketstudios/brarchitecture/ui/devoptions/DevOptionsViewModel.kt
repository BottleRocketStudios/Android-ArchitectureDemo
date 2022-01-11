package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.app.Application
import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogic
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class DevOptionsViewModel(
    private val app: Application,
    private val forceCrashLogicImpl: ForceCrashLogic,
    private val environmentRepository: EnvironmentRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel() {

    // ////////////////// ENVIRONMENT SECTION ////////////////// //
    val environmentNames: StateFlow<List<String>> = MutableStateFlow(environmentRepository.environments.map { it.environmentType.shortName })
    var environmentSpinnerPosition = environmentRepository.environments.indexOf(environmentRepository.selectedConfig)
        private set

    val baseUrl: StateFlow<String> = MutableStateFlow("")
    val message: StateFlow<String> = MutableStateFlow("")

    // ////////////////// FEATURE FLAG SECTION ////////////////// //
    // add project specific things here

    // ////////////////// APP INFO SECTION ////////////////// //
    val appVersionName: MutableStateFlow<String> = MutableStateFlow("")
    val appVersionCode: MutableStateFlow<String> = MutableStateFlow("")
    val appId: MutableStateFlow<String> = MutableStateFlow("")
    val buildIdentifier: MutableStateFlow<String> = MutableStateFlow("")

    init {
        updateEnvironmentInfo()
    }

    fun onEnvironmentChanged(newEnvironmentIndex: Int) {
        val newEnvironment = environmentRepository.environments[newEnvironmentIndex]
        val oldEnvironment = environmentRepository.selectedConfig
        Timber.v("[onEnvironmentChanged] newEnvironment=$newEnvironment, oldEnvironment=$oldEnvironment")
        if (newEnvironment != oldEnvironment) {
            environmentSpinnerPosition = newEnvironmentIndex
            environmentRepository.changeEnvironment(environmentRepository.environments[newEnvironmentIndex].environmentType)
            updateEnvironmentInfo()
            message.set("!!! Restart required !!!")
        } else {
            Timber.v("[onEnvironmentChanged] no changes needed as the same environment has been selected")
        }
    }

    fun onRestartCtaClick() {
        Timber.v("[onRestartCtaClick] restarting app now...")
        ProcessPhoenix.triggerRebirth(app)
    }

    fun onForceCrashCtaClicked() {
        Timber.v("[onForceCrashCtaClicked] forcing crash now...")
        forceCrashLogicImpl.forceCrashNow()
    }

    private fun updateEnvironmentInfo() {
        baseUrl.set(environmentRepository.selectedConfig.baseUrl)
    }
}
