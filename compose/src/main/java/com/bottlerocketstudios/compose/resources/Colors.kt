package com.bottlerocketstudios.compose.resources

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

val lightColors = lightColors(
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
