package com.bottlerocketstudios.brarchitecture.ui.bindingadapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
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
