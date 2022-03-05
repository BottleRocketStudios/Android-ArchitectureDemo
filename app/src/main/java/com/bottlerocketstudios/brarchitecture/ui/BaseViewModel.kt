package com.bottlerocketstudios.brarchitecture.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationObserver
import com.bottlerocketstudios.brarchitecture.navigation.NavigationEvent
import com.bottlerocketstudios.brarchitecture.navigation.NavigationObserver
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

/** Provides [LiveEvent]s for both navigation and external navigation and helper functionality to observe the [LiveData] from a [Fragment] */
abstract class BaseViewModel : ViewModel(), KoinComponent {
    protected val dispatcherProvider: DispatcherProvider by inject()

    /**
     * Helper to launch to IO thread quickly
     */
    fun launchIO(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(dispatcherProvider.IO, block = block)

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

    /** Helper function to avoid needing downcast declarations for public MutableStateFlow */
    protected fun <T : Any?> StateFlow<T?>?.setNullable(value: T?) {
        if (this is MutableStateFlow<T?>) {
            this.value = value
        } else {
            Timber.w("[set] unable to setValue for $this")
        }
    }
    /** Helper function to avoid needing downcast declarations for public MutableStateFlow. [value] only set when it is non-nullable */
    protected fun <T : Any> StateFlow<T>?.set(value: T?) {
        if (this is MutableStateFlow<T> && value != null) {
            this.value = value
        } else {
            Timber.w("[set] unable to set value for $this")
        }
    }

    /** Helper function to avoid needing downcast declarations for public MutableSharedFlow */
    protected suspend fun <T : Any?> SharedFlow<T?>?.emitNullable(value: T?) {
        if (this is MutableSharedFlow<T?>) {
            emit(value)
        } else {
            Timber.w("[set] unable to emit value for $this")
        }
    }
    /** Helper function to avoid needing downcast declarations for public MutableSharedFlow. [value] only emitted when it is non-nullable */
    protected suspend fun <T : Any> SharedFlow<T>?.emitValue(value: T?) {
        if (this is MutableSharedFlow<T> && value != null) {
            emit(value)
        } else {
            Timber.w("[emitValue] unable to emit value for $this")
        }
    }

    // Ties flow to viewModelScope to give StateFlow.
    fun <T> Flow<T>.groundState(initialValue: T) = this.stateIn(viewModelScope, SharingStarted.Lazily, initialValue)

    /** Setup observations for both [navigationEvent] from [fragment] */
    fun observeNavigationEvents(fragment: Fragment) {
        navigationEvent.observe(fragment.viewLifecycleOwner, NavigationObserver(fragment.findNavController()))
        externalNavigationEvent.observe(fragment.viewLifecycleOwner, ExternalNavigationObserver(fragment.requireActivity()))
    }
}
