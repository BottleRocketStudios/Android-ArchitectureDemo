package com.bottlerocketstudios.brarchitecture.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber

/**
 * [Marker interface](https://en.wikipedia.org/wiki/Marker_interface_pattern) designating that the denoted class is a Databinding ViewModel, the last VM of the traditional MVVM
 * (Model-View-ViewModel) pattern. The implementor usually takes in a data class with values that represent current state and provides databinding "observable" wrappers around the formatted data.
 *
 * See https://developer.android.com/topic/libraries/data-binding/observability
 */
interface DbViewModel {

    /** Helper function to avoid needing downcast declarations for public MutableLiveData or LiveEvent */
    fun <T> LiveData<T>.set(value: T?) = (this as? MutableLiveData<T>)?.setValue(value) ?: run { Timber.w("[set] unable to setValue for $this") }

    /** Helper function to avoid needing downcast declarations for public MutableLiveData or LiveEvent */
    fun <T> LiveData<T>.postValue(value: T?) = (this as? MutableLiveData<T>)?.postValue(value) ?: run { Timber.w("[postValue] unable to postValue for $this") }
}
