package com.bottlerocketstudios.brarchitecture.ui.util

import com.bottlerocketstudios.brarchitecture.ui.NavKey
import com.bottlerocketstudios.brarchitecture.ui.Routes

fun MutableList<NavKey>.navigateAsTopLevel(route: NavKey) {
    clear()
    add(route)
}

fun MutableList<NavKey>.popToMainInclusive() {
    val mainIndex = indexOf(Routes.Main)
    if (mainIndex != -1) {
        // Remove everything from Main onwards (inclusive)
        while (size > mainIndex) {
            removeAt(size - 1)
        }
    }
}
