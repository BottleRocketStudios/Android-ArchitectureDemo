package com.bottlerocketstudios.brarchitecture.navigation

import android.app.Activity
import android.content.Intent

/**
 * Encapsulates external activity intent navigation action.
 *
 * @see ExternalNavigationObserver
 */
data class ExternalNavigationEvent(val intent: Intent?) {

    /** Contains logic to navigate using [Activity.startActivity] (intended to be called from [ExternalNavigationObserver]) */
    fun navigate(activity: Activity) {
        intent?.let {
            activity.startActivity(it)
        }
    }
}
