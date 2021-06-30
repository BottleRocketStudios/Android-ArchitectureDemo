package com.bottlerocketstudios.brarchitecture.ui.bindingadapters

import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.util.NoFilterArrayAdapter
import timber.log.Timber

// Need both to be handled in same function as it can happen that the initialSelection is called prior to setting the adapter
@BindingAdapter(value = ["entries", "initialSelection"], requireAll = false)
fun <T : Any> AutoCompleteTextView.setAdapterWithInitialSelection(entries: List<T>?, position: Int?) {
    if (entries != null && position != null) {
        setAdapterWithInitialSelectionWorker(entries, position)
    }
}

// Need both to be handled in same function as it can happen that the initialSelection is called prior to setting the adapter
@BindingAdapter(value = ["entries", "initialSelection"], requireAll = false)
fun AutoCompleteTextView.setAdapterWithInitialSelectionArray(entries: Array<String>?, position: Int?) {
    if (entries != null && position != null) {
        setAdapterWithInitialSelectionWorker(entries.asList(), position)
    }
}

/**
 * Happy path: [AutoCompleteTextView.setAdapter] with [entries] and text set to value at [position].
 * Empty [entries] edge case: When [entries] isEmpty, then the [AutoCompleteTextView] parent (should be the wrapping [com.google.android.material.textfield.TextInputLayout]) visibility set to [View.GONE]
 * Position out of range edge case: When [position] is not the valid indices of [entries], fall back to zero.
 */
private fun <T : Any> AutoCompleteTextView.setAdapterWithInitialSelectionWorker(entries: List<T>, position: Int) {
    if (entries.isEmpty()) {
        (parent as ViewGroup).visibility = View.GONE
        Timber.d("[setAdapterWithInitialSelectionWorker] entries list is empty - updating parent view visibility to gone")
        return
    }
    val adjustedPosition = if (position !in entries.indices) {
        Timber.d("[setAdapterWithInitialSelectionWorker] defaulting to index 0 as index '$position' outside list range - list size ${entries.size}")
        0
    } else {
        position
    }
    (parent as ViewGroup).visibility = View.VISIBLE
    val adapter = NoFilterArrayAdapter(context, R.layout.dropdown_menu_popup_item, entries)
    setAdapter(adapter)
    setText(adapter.getItem(adjustedPosition).toString())
}
