package com.bottlerocketstudios.compose.navdrawer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NavItemState(
        @param:DrawableRes val icon: Int,
        @param:StringRes val itemText: Int,
        val selected: Boolean,
        val nestedMenuItems: List<NavItemState> = emptyList(),
        val onClick: suspend () -> Unit
)
