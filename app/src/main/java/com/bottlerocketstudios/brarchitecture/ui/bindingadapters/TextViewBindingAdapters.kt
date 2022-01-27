package com.bottlerocketstudios.brarchitecture.ui.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bottlerocketstudios.brarchitecture.ui.util.StringIdHelper

@BindingAdapter("textByStringIdHelper")
fun TextView.textByStringIdHelper(stringIdHelper: StringIdHelper?) {
    text = stringIdHelper?.getString(context)
}
