package com.bottlerocketstudios.brarchitecture.navigation

import androidx.lifecycle.Observer
import androidx.navigation.NavController
import timber.log.Timber

/**
 * Encapsulates navigation of [NavigationEvent]s via [navController].
 *
 * See [ExternalNavigationObserver] for external navigation.
 */
class NavigationObserver(private val navController: NavController) : Observer<NavigationEvent> {
    override fun onChanged(event: NavigationEvent) {
        Timber.v("[onChanged] event=$event")
        event.navigate(navController)
    }
}
