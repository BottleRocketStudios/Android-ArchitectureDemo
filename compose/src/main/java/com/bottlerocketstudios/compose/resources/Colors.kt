package com.bottlerocketstudios.compose.resources

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val br_red = Color(0xffe2231a)
val br_dark_red = Color(0xff71110d)
val br_light_red = Color(0xffe66963)
val brown_grey = Color(0xff979797)
val greyish_brown = Color(0xff515151)
val white = Color(0xffffffff)
val background_black = Color(0x05000000)
val black = Color(0xff000000)
val tertiary = Color(0xff1fada8)
val brown_grey_two = Color(0xff797979)
val brown_grey_three = Color(0xff9e9e9e)

val lightColors = ArchitectureDemoColors(
    tertiary = tertiary,
    brownGrey = brown_grey,
    materialColors = lightColors(
        primary = br_red,
        primaryVariant = br_dark_red,
        secondary = br_light_red,
        background = background_black,
        surface = white,
        onPrimary = white,
        onSecondary = white,
        onBackground = black,
        onSurface = greyish_brown,
    )
)

data class ArchitectureDemoColors(
    val tertiary: Color,
    val brownGrey: Color,
    val materialColors: Colors
) {
    val primary: Color
        get() = materialColors.primary

    val primaryVariant: Color
        get() = materialColors.primaryVariant

    val secondary: Color
        get() = materialColors.secondary

    val secondaryVariant: Color
        get() = materialColors.secondaryVariant

    val background: Color
        get() = materialColors.background

    val surface: Color
        get() = materialColors.surface

    val error: Color
        get() = materialColors.error

    val onPrimary: Color
        get() = materialColors.onPrimary

    val onSecondary: Color
        get() = materialColors.onSecondary

    val onBackground: Color
        get() = materialColors.onBackground

    val onSurface: Color
        get() = materialColors.onSurface

    val onError: Color
        get() = materialColors.onError

    val isLight: Boolean
        get() = materialColors.isLight
}
