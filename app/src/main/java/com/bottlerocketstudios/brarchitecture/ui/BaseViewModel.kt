package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationObserver
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.navigation.NavigationObserver
import com.hadilq.liveevent.LiveEvent

/** Provides [LiveEvent]s for both navigation and external navigation and helper functionality to observe the [LiveData] from a [Fragment] */
abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    /** Use to send [NavigationEvent]s (from subclasses) */
    protected val _navigationEvent = LiveEvent<NavigationEvent>()

    /** Note: You probably don't need to be using this, as [observeNavigationEvents] is likely handling the observer setup for you. Available if necessary. */
    val navigationEvent: LiveData<NavigationEvent> = _navigationEvent

    /** Use to send [ExternalNavigationEvent]s (from subclasses) */
    protected val _externalNavigationEvent = LiveEvent<ExternalNavigationEvent>()

    /** Note: You probably don't need to be using this, as [observeNavigationEvents] is likely handling the observer setup for you. Available if necessary. */
    val externalNavigationEvent: LiveData<ExternalNavigationEvent> = _externalNavigationEvent

    /** Setup observations for both [navigationEvent] from [fragment] */
    fun observeNavigationEvents(fragment: Fragment) {
        navigationEvent.observe(fragment.viewLifecycleOwner, NavigationObserver(fragment.findNavController()))
        externalNavigationEvent.observe(fragment.viewLifecycleOwner, ExternalNavigationObserver(fragment.requireActivity()))
    }
}
