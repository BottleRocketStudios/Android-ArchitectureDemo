package com.bottlerocketstudios.brarchitecture.ui.util

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.bottlerocketstudios.brarchitecture.ui.Routes

fun NavController.navigateAsTopLevel(route: String) = navigate(route) { popToMainInclusive() }

fun NavOptionsBuilder.popToMainInclusive() {
    popUpTo(Routes.Main) {
        inclusive = true
    }
}
