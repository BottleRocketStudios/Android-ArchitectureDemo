package com.bottlerocketstudios.brarchitecture.ui.util

import android.content.res.Resources
import android.view.View

/** Returns the string name for the resource id or "no-id" if there is no id set on the resource. */
fun View?.idName(): String {
    if (this == null) return ""
    return id.resourceIdName(resources)
}

/** Returns the string name for the resource id or "no-id" if there is no id set on the resource. */
fun Int.resourceIdName(resources: Resources): String {
    return if (this == -0x1) {
        "no-id"
    } else {
        resources.getResourceEntryName(this)
    }
}
