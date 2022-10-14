package com.bottlerocketstudios.compose.resources

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val br_red = Color(0xffe2231a)
val br_dark_red = Color(0xff71110d)
val br_light_red = Color(0xffe66963)
val brown_grey = Color(0xff979797)
val greyish_brown = Color(0xff515151)
val white = Color(0xffffffff)
val mostly_white = Color(0xfff8f8f8)
val black = Color(0xff000000)
val sea_foam = Color(0xff1fada8)
val transparent = Color(0x00000000)

val lightColors = ArchitectureDemoColors(
    tertiary = sea_foam,
    borderColor = brown_grey,
    materialColors = lightColors(
        primary = br_red,
        primaryVariant = br_dark_red,
        secondary = br_light_red,
        background = mostly_white,
        surface = white,
        onPrimary = white,
        onSecondary = white,
        onBackground = black,
        onSurface = greyish_brown,
    )
)

val darkColors = ArchitectureDemoColors(
    tertiary = Color.Green,
    borderColor = Color.White,
    materialColors = darkColors(
        primary = br_red,
        primaryVariant = br_dark_red,
        secondary = br_light_red,
        background = black,
        surface = greyish_brown,
        onPrimary = sea_foam,
        onSecondary = Color.Yellow,
        onBackground = Color.White,
        onSurface = Color.White,
    )
)

data class ArchitectureDemoColors(
    val tertiary: Color,
    val borderColor: Color,
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
