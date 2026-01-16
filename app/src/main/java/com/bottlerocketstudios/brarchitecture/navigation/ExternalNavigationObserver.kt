package com.bottlerocketstudios.brarchitecture.navigation

import android.app.Activity
import androidx.lifecycle.Observer
import timber.log.Timber

/**
 * Encapsulates external activity intent navigation
 *
 * @see ExternalNavigationEvent
 */
class ExternalNavigationObserver(private val activity: Activity) :
        Observer<ExternalNavigationEvent> {
    override fun onChanged(value: ExternalNavigationEvent) {
        Timber.v("[onChanged] event=$value")
        value.navigate(activity)
    }
}
