@file:Suppress("ConstructorParameterNaming", "MatchingDeclarationName", "LongParameterList")
package com.bottlerocketstudios.compose.resources

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// https://proandroiddev.com/supporting-different-screen-sizes-on-android-with-jetpack-compose-f215c13081bd
/**
 * This Dimensions class is intended to provide a logical breakdown of the system that Material
 * design uses for spacing and elevation. The "grid" properties represent multiples of a base grid size
 * that can be used for spacing elements in the xy plane (ex. padding, margins, etc). The "plane"
 * properties are similar but intended for the z plane (ex. elevation). By using this system for
 * all composable elements, it allows for the dimen class to be replaced if the width of the screen
 * changes. This means that smaller values can be substituted for smaller screens, allowing more screen
 * space to be reclaimed for content. For larger screens, such as tablets, it is still recommended to
 * create composables specifically to take advantage of the extra space more efficiently.
 */

class Dimensions(
    val grid_0_25: Dp,
    val grid_0_5: Dp,
    val grid_0_75: Dp,
    val grid_1: Dp,
    val grid_1_25: Dp,
    val grid_1_5: Dp,
    val grid_1_75: Dp,
    val grid_2: Dp,
    val grid_2_5: Dp,
    val grid_3: Dp,
    val grid_3_5: Dp,
    val grid_4: Dp,
    val grid_4_5: Dp,
    val grid_5: Dp,
    val grid_5_5: Dp,
    val grid_6: Dp,
    val grid_7: Dp,
    val grid_12_5: Dp,
    val grid_24: Dp,
    val plane_0: Dp,
    val plane_1: Dp,
    val plane_2: Dp,
    val plane_3: Dp,
    val plane_4: Dp,
    val plane_5: Dp,
    val minimum_touch_target: Dp = 48.dp,
)

val smallDimensions = Dimensions(
    grid_0_25 = 1.5f.dp,
    grid_0_5 = 3.dp,
    grid_0_75 = 5.dp,
    grid_1 = 6.dp,
    grid_1_25 = 8.dp,
    grid_1_5 = 9.dp,
    grid_1_75 = 11.dp,
    grid_2 = 12.dp,
    grid_2_5 = 15.dp,
    grid_3 = 18.dp,
    grid_3_5 = 21.dp,
    grid_4 = 24.dp,
    grid_4_5 = 27.dp,
    grid_5 = 30.dp,
    grid_5_5 = 33.dp,
    grid_6 = 36.dp,
    grid_7 = 42.dp,
    grid_12_5 = 75.dp,
    grid_24 = 144.dp,
    plane_0 = 0.dp,
    plane_1 = 1.dp,
    plane_2 = 2.dp,
    plane_3 = 3.dp,
    plane_4 = 6.dp,
    plane_5 = 12.dp,
)

val sw360Dimensions = Dimensions(
    grid_0_25 = 2.dp,
    grid_0_5 = 4.dp,
    grid_0_75 = 6.dp,
    grid_1 = 8.dp,
    grid_1_25 = 10.dp,
    grid_1_5 = 12.dp,
    grid_1_75 = 14.dp,
    grid_2 = 16.dp,
    grid_2_5 = 20.dp,
    grid_3 = 24.dp,
    grid_3_5 = 28.dp,
    grid_4 = 32.dp,
    grid_4_5 = 36.dp,
    grid_5 = 40.dp,
    grid_5_5 = 44.dp,
    grid_6 = 48.dp,
    grid_7 = 56.dp,
    grid_12_5 = 100.dp,
    grid_24 = 192.dp,
    plane_0 = 0.dp,
    plane_1 = 1.dp,
    plane_2 = 2.dp,
    plane_3 = 4.dp,
    plane_4 = 8.dp,
    plane_5 = 16.dp,
)
