package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogic
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class DevOptionsViewModel(
    private val app: Application,
    private val forceCrashLogicImpl: ForceCrashLogic,
    private val environmentRepository: EnvironmentRepository,
    private val dispatcherProvider: DispatcherProvider,
    buildConfigProvider: BuildConfigProvider,
) : BaseViewModel(app) {

    // ////////////////// ENVIRONMENT SECTION ////////////////// //
    val environmentNames: StateFlow<List<String>> = MutableStateFlow(environmentRepository.environments.map { it.environmentType.shortName })
    var environmentSpinnerPosition = environmentRepository.environments.indexOf(environmentRepository.selectedConfig)
        private set

    val baseUrl: StateFlow<String> = MutableStateFlow("")

    // ////////////////// FEATURE FLAG SECTION ////////////////// //
    // add project specific things here

    // ////////////////// APP INFO SECTION ////////////////// //
    val appVersionName: StateFlow<String> = MutableStateFlow("")
    val appVersionCode: StateFlow<String> = MutableStateFlow("")
    val appId: StateFlow<String> = MutableStateFlow("")
    val buildIdentifier: StateFlow<String> = MutableStateFlow("")

    // ////////////////// EVENT OBJECTS ////////////////// //
    val eventFlow: SharedFlow<DevOptionsEvent> = MutableSharedFlow()

    sealed class DevOptionsEvent {
        data class MessageToUserEvent(val message: String) : DevOptionsEvent()
        data class EnvironmentDropdownDismissedEvent(val unit: Unit) : DevOptionsEvent()
    }

    init {
        updateEnvironmentInfo()
        appVersionName.set(app.packageManager!!.getPackageInfo(app.packageName, 0).versionName)
        appVersionCode.set(app.packageManager!!.getPackageInfo(app.packageName, 0).versionCode.toString())
        appId.set(app.packageName)
        buildIdentifier.set(buildConfigProvider.buildIdentifier)
    }

    fun onEnvironmentChanged(newEnvironmentIndex: Int) {
        val newEnvironment = environmentRepository.environments[newEnvironmentIndex]
        val oldEnvironment = environmentRepository.selectedConfig
        Timber.v("[onEnvironmentChanged] newEnvironment=$newEnvironment, oldEnvironment=$oldEnvironment")
        if (newEnvironment != oldEnvironment) {
            environmentSpinnerPosition = newEnvironmentIndex
            environmentRepository.changeEnvironment(environmentRepository.environments[newEnvironmentIndex].environmentType)
            updateEnvironmentInfo()
            sendMessageToUser("!!! Restart required !!!")
        } else {
            Timber.v("[onEnvironmentChanged] no changes needed as the same environment has been selected")
        }
    }

    fun onEnvironmentDropdownDismissed() = viewModelScope.launch(dispatcherProvider.IO) {
        eventFlow.emitValue(DevOptionsEvent.EnvironmentDropdownDismissedEvent(Unit))
    }

    private fun sendMessageToUser(message: String) = viewModelScope.launch {
        eventFlow.emitValue(DevOptionsEvent.MessageToUserEvent(message))
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
