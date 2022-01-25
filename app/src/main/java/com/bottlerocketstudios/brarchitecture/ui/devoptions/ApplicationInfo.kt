package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.app.Application
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider

data class ApplicationInfo(
    val appVersionName: String,
    val appVersionCode: String,
    val appId: String,
    val buildIdentifier: String,
)

interface ApplicationInfoManager {
    fun getApplicationInfo(): ApplicationInfo
}

class ApplicationInfoManagerImpl(private val app: Application, private val buildConfigProvider: BuildConfigProvider) : ApplicationInfoManager {
    override fun getApplicationInfo(): ApplicationInfo {
        return ApplicationInfo(
            appVersionName = app.packageManager!!.getPackageInfo(app.packageName, 0).versionName,
            appVersionCode = app.packageManager!!.getPackageInfo(app.packageName, 0).versionCode.toString(),
            appId = app.packageName,
            buildIdentifier = buildConfigProvider.buildIdentifier
        )
    }
}
