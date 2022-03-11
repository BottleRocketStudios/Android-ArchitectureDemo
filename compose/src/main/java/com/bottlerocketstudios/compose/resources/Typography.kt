package com.bottlerocketstudios.compose.resources

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val typography = Typography(
    defaultFontFamily = FontFamily.SansSerif,
    h1 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal
    ),
    h2 = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal,
        letterSpacing = 0.03.sp
    ),
    h3 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal,
        letterSpacing = 0.03.sp
    ),
    h4 = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal,
        letterSpacing = 0.03.sp
    ),
    h5 = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Normal,
        letterSpacing = 0.03.sp
    ),
    h6 = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
        letterSpacing = 0.03.sp
    ),
    body1 = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
        letterSpacing = 0.03.sp
    )
)

// fun TextStyle.light() = this.copy(fontWeight = FontWeight.Light)
// fun TextStyle.normal() = this.copy(fontWeight = FontWeight.Normal)
// fun TextStyle.bold() = this.copy(fontWeight = FontWeight.Bold)
