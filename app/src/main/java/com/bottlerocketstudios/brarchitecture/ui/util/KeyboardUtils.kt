package com.bottlerocketstudios.brarchitecture.ui.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import timber.log.Timber

// Inspiration from https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
/** Hides soft input keyboard from an activity. */
fun Activity?.hideKeyboard(clearFocus: Boolean = true) {
    if (this != null) {
        if (clearFocus) {
            clearFocusedView()
        }
        val imm = application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val windowToken = window.decorView.rootView.windowToken
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

/** Clears the focused view on the activity */
private fun Activity?.clearFocusedView() {
    this?.currentFocus
        ?.also { Timber.v("[clearFocusedView] clearing focus on view=$it") }
        ?.clearFocus()
}
