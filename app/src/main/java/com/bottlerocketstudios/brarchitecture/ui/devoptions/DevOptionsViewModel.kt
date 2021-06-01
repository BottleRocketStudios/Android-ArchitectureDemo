package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogic
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.hadilq.liveevent.LiveEvent
import com.jakewharton.processphoenix.ProcessPhoenix
import timber.log.Timber

class DevOptionsViewModel(
    private val app: Application,
    private val forceCrashLogicImpl: ForceCrashLogic,
    private val environmentRepository: EnvironmentRepository,
    buildConfigProvider: BuildConfigProvider,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel(app) {

    // ////////////////// ENVIRONMENT SECTION ////////////////// //
    val environmentNames: LiveData<List<String>> = MutableLiveData(environmentRepository.environments.map { it.environmentType.shortName })
    var environmentSpinnerPosition = environmentRepository.environments.indexOf(environmentRepository.selectedConfig)
        private set

    val baseUrl: LiveData<String> = MutableLiveData()

    // ////////////////// FEATURE FLAG SECTION ////////////////// //
    // add project specific things here

    // ////////////////// APP INFO SECTION ////////////////// //
    val appVersionName: LiveData<String>
    val appVersionCode: LiveData<String>
    val appId: LiveData<String>
    val buildIdentifier: LiveData<String>

    // ////////////////// MISCELLANEOUS ////////////////// //
    val messageToUser: LiveData<String> = LiveEvent()
    val environmentDropdownDismissed: LiveData<Unit> = LiveEvent()

    init {
        updateEnvironmentInfo()
        appVersionName = MutableLiveData(app.packageManager!!.getPackageInfo(app.packageName, 0).versionName)
        appVersionCode = MutableLiveData(app.packageManager!!.getPackageInfo(app.packageName, 0).versionCode.toString())
        appId = MutableLiveData(app.packageName)
        buildIdentifier = MutableLiveData(buildConfigProvider.buildIdentifier)
    }

    fun onEnvironmentChanged(newEnvironmentIndex: Int) {
        val newEnvironment = environmentRepository.environments[newEnvironmentIndex]
        val oldEnvironment = environmentRepository.selectedConfig
        Timber.v("[onEnvironmentChanged] newEnvironment=$newEnvironment, oldEnvironment=$oldEnvironment")
        if (newEnvironment != oldEnvironment) {
            environmentSpinnerPosition = newEnvironmentIndex
            environmentRepository.changeEnvironment(environmentRepository.environments[newEnvironmentIndex].environmentType)
            updateEnvironmentInfo()
            messageToUser.set("!!! Restart required !!!")
        } else {
            Timber.v("[onEnvironmentChanged] no changes needed as the same environment has been selected")
        }
    }

    fun onEnvironmentDropdownDismissed() {
        environmentDropdownDismissed.postValue(Unit)
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
