package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationObserver
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.navigation.NavigationObserver
import com.hadilq.liveevent.LiveEvent
import timber.log.Timber

/** Provides [LiveEvent]s for both navigation and external navigation and helper functionality to observe the [LiveData] from a [Fragment] */
abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    /**
     * Use to send [NavigationEvent]s (from subclasses).
     *
     * Note: You probably don't need to be observing this, as [observeNavigationEvents] is likely handling the observer setup for you. Available if necessary.
     */
    val navigationEvent: LiveData<NavigationEvent> = LiveEvent<NavigationEvent>()

    /**
     * Use to send [ExternalNavigationEvent]s (from subclasses).
     *
     * Note: You probably don't need to be observing this, as [observeNavigationEvents] is likely handling the observer setup for you. Available if necessary.
     */
    val externalNavigationEvent: LiveData<ExternalNavigationEvent> = LiveEvent<ExternalNavigationEvent>()

    /** Helper function to avoid needing downcast declarations for public MutableLiveData or LiveEvent */
    protected fun <T> LiveData<T>.set(value: T?) = (this as? MutableLiveData<T>)?.setValue(value) ?: run { Timber.w("[set] unable to setValue for $this") }

    /** Helper function to avoid needing downcast declarations for public MutableLiveData or LiveEvent */
    protected fun <T> LiveData<T>.postValue(value: T?) = (this as? MutableLiveData<T>)?.postValue(value) ?: run { Timber.w("[postValue] unable to postValue for $this") }

    /** Setup observations for both [navigationEvent] from [fragment] */
    fun observeNavigationEvents(fragment: Fragment) {
        navigationEvent.observe(fragment.viewLifecycleOwner, NavigationObserver(fragment.findNavController()))
        externalNavigationEvent.observe(fragment.viewLifecycleOwner, ExternalNavigationObserver(fragment.requireActivity()))
    }
}
