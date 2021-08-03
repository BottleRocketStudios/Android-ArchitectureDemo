package com.bottlerocketstudios.brarchitecture.ui.bindingadapters

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.util.hideKeyboard
import com.bumptech.glide.Glide

/** Basically [View.GONE] when [value] is false. If true, set to [View.VISIBLE]. */
@BindingAdapter("visibilityGoneIfFalse")
fun View.setVisibilityGoneIfFalse(value: Boolean) {
    visibility = if (!value) View.GONE else View.VISIBLE
}

/** Basically [View.GONE] when [value] is null. If present (not-null), set to [View.VISIBLE]. Opposite effect of [setVisibilityVisibleIfNull] */
@BindingAdapter("visibilityGoneIfNull")
fun View.setVisibilityGoneIfNull(value: Any?) {
    visibility = if (value == null) View.GONE else View.VISIBLE
}

/** Basically [View.VISIBLE] when [value] is null. If present (not-null), set to [View.GONE]. Opposite effect of [setVisibilityGoneIfNull] */
@BindingAdapter("visibilityVisibleIfNull")
fun View.setVisibilityVisibleIfNull(value: Any?) {
    visibility = if (value == null) View.VISIBLE else View.GONE
}

/** Basically [View.GONE] when the String [value] `isNullOrEmpty` is true. */
@BindingAdapter("visibilityGoneIfNullOrEmpty")
fun View.setVisibilityGoneIfNullOrEmpty(value: String?) {
    visibility = if (value.isNullOrEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(this)
            .load(imageUrl)
            .error(drawable)
            .into(this)
    }
}

// TODO: TEMPLATE - Remove this extension function when creating a new project
@BindingAdapter("fileType")
fun AppCompatImageView.setFileType(fileType: String?) {
    Glide.with(this)
        .load(if (fileType.equals("commit_directory")) R.drawable.ic_folders else R.drawable.ic_file)
        .into(this)
}

/**
 * Functional style interface to represent a callback with no arguments and no return type. Similar to [Runnable] but without the thread execution awareness.
 */
interface GeneralCallback {
    fun run()
}

/**
 * Sends an empty callback when the soft keyboard done/send/go is pressed OR the keyboard enter key is pressed AND hides the keyboard.
 *
 * ```xml
 * app:onEditorEnterAction="@{() -> reservationLookupViewModel.retrieveReservationClicked()}"
 * ```
 */
@BindingAdapter("onEditorEnterAction")
fun EditText.onEditorEnterAction(enterCallback: GeneralCallback?) {
    if (enterCallback == null) {
        setOnEditorActionListener(null)
    } else {
        setOnEditorActionListener { v, actionId, event ->
            val validImeAction = when (actionId) {
                EditorInfo.IME_ACTION_DONE,
                EditorInfo.IME_ACTION_SEND,
                EditorInfo.IME_ACTION_GO -> true
                else -> false
            }
            val validKey = when (event?.keyCode) {
                KeyEvent.KEYCODE_ENTER,
                KeyEvent.KEYCODE_DPAD_CENTER,
                KeyEvent.KEYCODE_NUMPAD_ENTER -> true
                else -> false
            }
            val validKeyDown = validKey && event.action == KeyEvent.ACTION_DOWN

            if (validImeAction || validKeyDown) {
                enterCallback.run()
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }
}
