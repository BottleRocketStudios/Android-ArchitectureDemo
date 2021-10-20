package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogic
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class DevOptionsViewModel(
    private val app: Application,
    private val forceCrashLogicImpl: ForceCrashLogic,
    private val environmentRepository: EnvironmentRepository,
    buildConfigProvider: BuildConfigProvider,
) : BaseViewModel(app) {

    // ////////////////// ENVIRONMENT SECTION ////////////////// //
    private val _environmentNames = MutableStateFlow(environmentRepository.environments.map { it.environmentType.shortName })
    val environmentNames: StateFlow<List<String>> = _environmentNames
    var environmentSpinnerPosition = environmentRepository.environments.indexOf(environmentRepository.selectedConfig)
        private set

    private val _baseUrl = MutableStateFlow<String?>("")
    val baseUrl: StateFlow<String?> = _baseUrl

    // ////////////////// FEATURE FLAG SECTION ////////////////// //
    // add project specific things here

    // ////////////////// APP INFO SECTION ////////////////// //
    private val _appVersionName = MutableStateFlow<String?>("")
    val appVersionName: StateFlow<String?> = _appVersionName
    private val _appVersionCode = MutableStateFlow<String?>("")
    val appVersionCode: StateFlow<String?> = _appVersionCode
    private val _appId = MutableStateFlow<String?>("")
    val appId: StateFlow<String?> = _appId
    private val _buildIdentifier = MutableStateFlow<String?>("")
    val buildIdentifier: StateFlow<String?> = _buildIdentifier

    // ////////////////// EVENT OBJECTS ////////////////// //
    private val eventChannel = Channel<DevOptionsEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    sealed class DevOptionsEvent {
        data class MessageToUserEvent(val message: String): DevOptionsEvent()
        data class EnvironmentDropdownDismissedEvent(val unit: Unit): DevOptionsEvent()
    }

    init {
        updateEnvironmentInfo()
        _appVersionName.value = app.packageManager!!.getPackageInfo(app.packageName, 0).versionName
        _appVersionCode.value = app.packageManager!!.getPackageInfo(app.packageName, 0).versionCode.toString()
        _appId.value =  app.packageName
        _buildIdentifier.value = buildConfigProvider.buildIdentifier
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

    fun onEnvironmentDropdownDismissed() = viewModelScope.launch {
        eventChannel.send(DevOptionsEvent.EnvironmentDropdownDismissedEvent(Unit))
    }

    private fun sendMessageToUser(message: String) = viewModelScope.launch {
        eventChannel.send(DevOptionsEvent.MessageToUserEvent(message))
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
        _baseUrl.value = environmentRepository.selectedConfig.baseUrl
    }
}
