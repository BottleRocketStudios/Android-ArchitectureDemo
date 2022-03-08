package com.bottlerocketstudios.compose.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle

@Composable
fun IconText(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    iconColor: Color = LocalContentColor.current,
    text: String,
    style: TextStyle
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = iconColor
        )
        Text(
            text = text,
            style = style
        )
    }
}
