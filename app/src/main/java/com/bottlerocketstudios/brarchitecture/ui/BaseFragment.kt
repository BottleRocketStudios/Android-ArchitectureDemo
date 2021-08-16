package com.bottlerocketstudios.brarchitecture.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment

/**
 * Manages navigation observer setup
 *
 * To create a new fragment:
 *
 * 1. Pass in the associated fragment ViewModel (extending [BaseViewModel]).
 * 2. Override [fragmentViewModel] (using `by viewModel()`, `by sharedViewModel()`, or similar function)
 */
abstract class BaseFragment<FRAGMENT_VIEW_MODEL : BaseViewModel> : Fragment() {

    /** Fragment scoped Android ViewModel. Primary ViewModel to interact with for the given Fragment. */
    protected abstract val fragmentViewModel: FRAGMENT_VIEW_MODEL

    /** Calls [setupNavigationObservers] and [setupBinding] */
    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationObservers()
    }

    /**
     * Observes [BaseViewModel.navigationEvent] with [NavigationObserver][com.bottlerocketstudios.brarchitecture.navigation.NavigationObserver] and
     * [BaseViewModel.externalNavigationEvent] with [ExternalNavigationObserver][com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationObserver]
     * to prevent all subclasses from writing the same line of code.
     *
     * Called in [onViewCreated]
     */
    @CallSuper
    protected open fun setupNavigationObservers() {
        fragmentViewModel.observeNavigationEvents(this)
    }
}
