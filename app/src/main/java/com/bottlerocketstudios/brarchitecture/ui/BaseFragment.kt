package com.bottlerocketstudios.brarchitecture.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.bottlerocketstudios.brarchitecture.BR

/**
 * Manages DataBinding setup and navigation observer setup, allows per fragment logic/customizations via [setupBinding]
 *
 * To create a new fragment:
 *
 * 1. Pass in the associated fragment ViewModel (extending [BaseViewModel]) and Binding on the [BaseFragment] subclass.
 * 2. Override [fragmentViewModel] (using `by viewModel()`, `by sharedViewModel()`, or similar function) and [getLayoutRes] with the fragment's `R.layout.foo` resource identifier.
 * 3. Override [setupBinding] instead of [onCreateView] or [onViewCreated].
 */
abstract class BaseFragment<FRAGMENT_VIEW_MODEL : BaseViewModel, BINDING : ViewDataBinding>() : Fragment() {

    /** Fragment scoped Android ViewModel. Primary ViewModel to interact with for the given Fragment. */
    protected abstract val fragmentViewModel: FRAGMENT_VIEW_MODEL

    // Use private nullable binding in order to set in onCreateView and onDestroyView
    private var _binding: BINDING? = null

    /**
     * Wraps private nullable property. Lifecycle valid from [onCreateView] to [onDestroyView].
     * Treat similar to [requireContext] which throws if context is null, meaning you are aware of when you can call it and should use it.
     */
    private val binding: BINDING
        get() = _binding!!

    /**
     * See [setupBinding] if you want to add logic post view creation.
     *
     * > It is recommended to **only** inflate the layout in this method and move logic that operates on the returned View to onViewCreated(View, Bundle).
     * > Source: https://developer.android.com/reference/androidx/fragment/app/Fragment#onCreateView(android.view.LayoutInflater,%20android.view.ViewGroup,%20android.os.Bundle)
     */
    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        return binding.root
    }

    /** Calls [setupNavigationObservers] and [setupBinding] */
    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationObservers()
        setupBinding(binding)
    }

    /** Use onDestroy(binding) if need access to [binding]. Otherwise, override this function and call super. */
    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyView(binding)
    }

    /**
     * Override per fragment to add any further binding related setup, make ViewModel function calls, and add ViewModel LiveData Observer setup blocks.
     *
     * Sets up the [binding], calling [ViewDataBinding.setLifecycleOwner] and attaching the layout "viewModel" variable with [fragmentViewModel] (called in [onViewCreated]
     *
     * **NOTE: You must use "viewModel" as the layout variable name**
     */
    @CallSuper
    open fun setupBinding(binding: BINDING) {
        binding.lifecycleOwner = this
        // In all layouts, the variable name should be "viewModel" for the given FRAGMENT_VIEW_MODEL
        binding.setVariable(BR.viewModel, fragmentViewModel)
    }

    /** Override per fragment to add any further onDestroyView related cleanup. */
    @CallSuper
    open fun onDestroyView(binding: BINDING) {
        // Make LeakCanary happy by nulling out binding reference here: https://stackoverflow.com/questions/57647751/android-databinding-is-leaking-memory
        _binding = null
    }

    /** Fragment layout id for the screen. (ex: R.layout.home_fragment) */
    @LayoutRes
    abstract fun getLayoutRes(): Int

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
