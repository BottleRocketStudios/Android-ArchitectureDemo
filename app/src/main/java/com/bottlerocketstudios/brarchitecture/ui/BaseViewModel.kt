package com.bottlerocketstudios.brarchitecture.ui

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bottlerocketstudios.brarchitecture.BuildConfig
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.util.logger.TAG_NAV
import com.bottlerocketstudios.brarchitecture.utils.error.buildExceptionErrorString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
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

@Suppress("TooManyFunctions")
/**
 * Provides common utility functionality for ViewModels including [SharedFlow]s for external
 * navigation
 */
abstract class BaseViewModel : ViewModel(), KoinComponent {
    // region DI
    protected val dispatcherProvider: DispatcherProvider by inject()
    protected val toaster: Toaster by inject()
    // endregion

    // region UI State
    val showLoadingIndicator = MutableStateFlow(false)
    // endregion

    // region Helpers
    /** Helper to launch to IO thread quickly */
    fun launchIO(block: suspend CoroutineScope.() -> Unit): Job =
            viewModelScope.launch(dispatcherProvider.IO, block = block)

    /**
     * Utility function to switch coroutine context to Main. Useful for making UI updates from IO
     */
    suspend fun runOnMain(block: suspend CoroutineScope.() -> Unit) =
            withContext(dispatcherProvider.Main, block)
    // endregion

    // region Error handling
    /** Used to display error message with standard UI pattern */
    suspend fun handleError(@StringRes messageId: Int) {
        runOnMain { toaster.toast(messageId) }
    }

    /** Used to display a notification message with standard UI pattern */
    suspend fun notifyUser(@StringRes messageId: Int) {
        runOnMain { toaster.toast(messageId) }
    }

    // endregion

    // region Navigation
    /**
     * Shared flow that behaves like event
     */
    fun <T> event(): SharedFlow<T> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /** Use to send [ExternalNavigationEvent]s (from subclasses). */
    val externalNavigationEvent: SharedFlow<ExternalNavigationEvent> = event()
    // endregion

    // region Helpers (continued)

    /**
     * Shared flow that behaves like event
     */
    fun <T> event(): SharedFlow<T> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * Helper functions to get access down casted mutable SharedFlows
     * ```
     *    due to SharedFlow being covariant we must use templates with upper bounds to show type errors at build instead of run time.
     * ```
     */
    protected suspend fun <T : Number?> SharedFlow<T>.emit(value: T) =
            (this as? MutableSharedFlow<T>)?.emit(value)
                    ?: run { Timber.w("[emitValue] unable to emit value for $this") }
    protected suspend fun <T : CharSequence> SharedFlow<T>.emit(value: T) =
            (this as? MutableSharedFlow<T>)?.emit(value)
                    ?: run { Timber.w("[emitValue] unable to emit value for $this") }
    protected suspend fun SharedFlow<Boolean>.emit(value: Boolean) =
            (this as? MutableSharedFlow<Boolean>)?.emit(value)
                    ?: run { Timber.w("[emitValue] unable to emit value for $this") }
    protected suspend fun SharedFlow<Unit>.emit(value: Unit) =
            (this as? MutableSharedFlow<Unit>)?.emit(value)
                    ?: run { Timber.w("[emitValue] unable to emit value for $this") }
    protected suspend fun <T : Any> SharedFlow<T>.emit(value: T) =
            (this as? MutableSharedFlow<T>)?.emit(value)
                    ?: run { Timber.w("[emitValue] unable to emit value for $this") }

    protected fun <T> SharedFlow<T>.tryEmit(value: T) =
        (this as? MutableSharedFlow<T>)?.tryEmit(value) ?: run {
            Timber.w("[tryEmitValue] unable to tryEmit value for $this")
            false
        }

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
    protected fun <T : Any> StateFlow<T>.setValue(value: T) {
        (this as? MutableStateFlow<T>)?.value = value
    }

    // Ties flow to viewModelScope to give StateFlow.
    fun <T> Flow<T>.groundState(initialValue: T) =
            this.stateIn(viewModelScope, SharingStarted.Eagerly, initialValue)
    // endregion

    // region API Response handling
    /**
     * Helper function to log throwable when a repository returns a failure. [useApiError] By
     * default, false. If set to true, will use error string from [APIException].
     */
    suspend fun <T> Result<T>.onFailureLogged(
            tag: String? = null,
            @StringRes errorStrId: Int = -1,
            useApiError: Boolean = false,
            action: ((Throwable) -> Unit)? = null,
    ): Result<T> = onFailure {
        Timber.tag(TAG_NAV).e(message = it.message, t = it)
        if (BuildConfig.DEBUG) {
            Timber.tag(TAG_NAV).e(message = it.buildExceptionErrorString())
        }

        runOnMain {
            if (useApiError) {
                toaster.toast(
                        it.buildExceptionErrorString(),
                        Toast.LENGTH_LONG,
                )
            } else if (errorStrId != -1) {
                toaster.toast(
                        errorStrId,
                        Toast.LENGTH_LONG,
                )
            } else {
                toaster.toast(
                        it.message ?: "Unknown Error",
                        Toast.LENGTH_LONG,
                )
            }
        }
        action?.invoke(it)
    }

    /** Allows wrapping loading indicator control logic. */
    suspend fun <T> MutableStateFlow<Boolean>.wrapIndicator(block: suspend () -> T): T {
        this.value = true
        val result = block()
        this.value = false
        return result
    }

    // endregion
}
