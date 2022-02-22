package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.app.Application
import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogic
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class DevOptionsViewModel(
    private val app: Application,
    private val forceCrashLogicImpl: ForceCrashLogic,
    private val applicationInfoManager: ApplicationInfoManager,
    private val environmentRepository: EnvironmentRepository,
    private val toaster: Toaster,
) : BaseViewModel() {

    // ////////////////// ENVIRONMENT SECTION ////////////////// //
    val environmentNames: StateFlow<List<String>> = MutableStateFlow(environmentRepository.environments.map { it.environmentType.shortName })
    val environmentSpinnerPosition: StateFlow<Int> = MutableStateFlow(environmentRepository.environments.indexOf(environmentRepository.selectedConfig))

    val baseUrl: StateFlow<String> = MutableStateFlow("")

    // ////////////////// FEATURE FLAG SECTION ////////////////// //
    // add project specific things here

    // ////////////////// APP INFO SECTION ////////////////// //
    val applicationInfo = applicationInfoManager.getApplicationInfo()

    init {
        updateEnvironmentInfo()
    }

    fun onEnvironmentChanged(newEnvironmentIndex: Int) {
        val newEnvironment = environmentRepository.environments[newEnvironmentIndex]
        val oldEnvironment = environmentRepository.selectedConfig
        Timber.v("[onEnvironmentChanged] newEnvironment=$newEnvironment, oldEnvironment=$oldEnvironment")
        if (newEnvironment != oldEnvironment) {
            environmentSpinnerPosition.set(newEnvironmentIndex)
            environmentRepository.changeEnvironment(environmentRepository.environments[newEnvironmentIndex].environmentType)
            updateEnvironmentInfo()
            toaster.toast("!!! Restart required !!!")
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
