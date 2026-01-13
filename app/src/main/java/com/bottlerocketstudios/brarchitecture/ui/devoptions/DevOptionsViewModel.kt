package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.app.Application
import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogic
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepository
import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import com.bottlerocketstudios.brarchitecture.domain.repositories.FeatureToggleRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.inject
import timber.log.Timber

class DevOptionsViewModel : BaseViewModel() {
    // region DI
    private val app: Application by inject()
    private val forceCrashLogicImpl: ForceCrashLogic by inject()
    private val applicationInfoManager: ApplicationInfoManager by inject()
    private val environmentRepository: EnvironmentRepository by inject()
    private val featureToggleRepository: FeatureToggleRepository by inject()
    // endregion

    // region Events
    val featureToggleClicked = MutableSharedFlow<Set<FeatureToggle>>()
    // endregion

    // region UI State
    val environmentNames: StateFlow<List<String>> =
            MutableStateFlow(
                    environmentRepository.environments.map { it.environmentType.shortName }
            )
    val environmentSpinnerPosition: StateFlow<Int> =
            MutableStateFlow(
                    environmentRepository.environments.indexOf(environmentRepository.selectedConfig)
            )
    val baseUrl: StateFlow<String> = MutableStateFlow("")
    val applicationInfo = applicationInfoManager.getApplicationInfo()
    // endregion

    // region Init
    init {
        updateEnvironmentInfo()
    }
    // endregion

    // region UI Callbacks

    fun onEnvironmentChanged(newEnvironmentIndex: Int) {
        val newEnvironment = environmentRepository.environments[newEnvironmentIndex]
        val oldEnvironment = environmentRepository.selectedConfig
        Timber.v(
                "[onEnvironmentChanged] newEnvironment=$newEnvironment, oldEnvironment=$oldEnvironment"
        )
        if (newEnvironment != oldEnvironment) {
            environmentSpinnerPosition.setValue(newEnvironmentIndex)
            environmentRepository.changeEnvironment(
                    environmentRepository.environments[newEnvironmentIndex].environmentType
            )
            updateEnvironmentInfo()
            toaster.toast("!!! Restart required !!!")
        } else {
            Timber.v(
                    "[onEnvironmentChanged] no changes needed as the same environment has been selected"
            )
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

    fun onFeatureToggleCtaClicked() {
        Timber.v("[onFeatureToggleCtaClicked] navigating to Feature Toggle Screen now...")
        launchIO { featureToggleClicked.emit(featureToggleRepository.featureToggles.value) }
    }

    private fun updateEnvironmentInfo() {
        baseUrl.setValue(environmentRepository.selectedConfig.baseUrl)
    }
    // endregion
}
