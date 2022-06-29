package com.bottlerocketstudios.brarchitecture.ui

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
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
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

/** Provides common utility functionality for ViewModels including [LiveEvent]s for external navigation */
abstract class BaseViewModel : ViewModel(), KoinComponent {
    protected val dispatcherProvider: DispatcherProvider by inject()
    protected val toaster: Toaster by inject()

    /**
     * Helper to launch to IO thread quickly
     */
    fun launchIO(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(dispatcherProvider.IO, block = block)

    /**
     * Utility function to switch coroutine context to Main.
     * Useful for making UI updates from IO
     */
    suspend fun runOnMain(block: suspend CoroutineScope.() -> Unit) =
        withContext(dispatcherProvider.Main, block)

    // /////////////////////////////////////////////////////////////////////////
    // Error handling
    // /////////////////////////////////////////////////////////////////////////
    /**
     * Used to display error message with standard UI pattern
     */
    suspend fun handleError(@StringRes messageId: Int) {
        runOnMain {
            toaster.toast(messageId)
        }
    }

    /**
     * Used to apply default error when handling Status and process a success block.
     */
    suspend inline fun <T : Any> Status<T>.handlingErrors(@StringRes messageId: Int, onSuccess: (T) -> Unit): Status<T> {
        if (this is Status.Success) {
            onSuccess(this.data)
        } else {
            handleError(messageId = messageId)
        }
        return this
    }

    /**
     * Use to send [ExternalNavigationEvent]s (from subclasses).
     */
    val externalNavigationEvent: LiveData<ExternalNavigationEvent> = LiveEvent<ExternalNavigationEvent>()

    /** Helper function to avoid needing downcast declarations for public MutableLiveData or LiveEvent */
    protected fun <T> LiveData<T>.set(value: T?) = (this as? MutableLiveData<T>)?.setValue(value) ?: run { Timber.w("[set] unable to setValue for $this") }

    /** Helper function to avoid needing downcast declarations for public MutableLiveData or LiveEvent */
    protected fun <T> LiveData<T>.postValue(value: T?) = (this as? MutableLiveData<T>)?.postValue(value) ?: run { Timber.w("[postValue] unable to postValue for $this") }

    /**
     *  Helper functions to get access down casted mutable SharedFlows
     *    due to SharedFlow being covariant we must use templates with upper bounds to show type errors at build instead of run time.
     */
    protected suspend fun <T : Number?> SharedFlow<T>.emit(value: T) =
        (this as? MutableSharedFlow<T>)?.emit(value) ?: run { Timber.w("[emitValue] unable to emit value for $this") }
    protected suspend fun <T : CharSequence> SharedFlow<T>.emit(value: T) =
        (this as? MutableSharedFlow<T>)?.emit(value) ?: run { Timber.w("[emitValue] unable to emit value for $this") }
    protected suspend fun SharedFlow<Boolean>.emit(value: Boolean) =
        (this as? MutableSharedFlow<Boolean>)?.emit(value) ?: run { Timber.w("[emitValue] unable to emit value for $this") }
    protected suspend fun SharedFlow<Unit>.emit(value: Unit) =
        (this as? MutableSharedFlow<Unit>)?.emit(value) ?: run { Timber.w("[emitValue] unable to emit value for $this") }

    /** Helper functions to avoid needing downcast declarations for public MutableStateFlow */
    protected fun <T : Number> StateFlow<T>.setValue(value: T) {
        (this as? MutableStateFlow<T>)?.value = value
    }
    protected fun <T : CharSequence> StateFlow<T>.setValue(value: T) {
        (this as? MutableStateFlow<T>)?.value = value
    }
    protected fun StateFlow<Boolean>.setValue(value: Boolean) {
        (this as? MutableStateFlow<Boolean>)?.value = value
    }
    protected fun StateFlow<Unit>.setValue(value: Unit) {
        (this as? MutableStateFlow<Unit>)?.value = value
    }

    // Ties flow to viewModelScope to give StateFlow.
    fun <T> Flow<T>.groundState(initialValue: T) = this.stateIn(viewModelScope, SharingStarted.Lazily, initialValue)
}
