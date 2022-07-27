package com.bottlerocketstudios.compose.navdrawer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NavItemState(
    @DrawableRes val icon: Int,
    @StringRes val itemText: Int,
    val selected: Boolean,
    val onClick: suspend () -> Unit
)
