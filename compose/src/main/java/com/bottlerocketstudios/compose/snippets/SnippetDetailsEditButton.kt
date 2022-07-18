package com.bottlerocketstudios.compose.snippets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography

@Composable
fun SnippetDetailsEditButton(
    icon: ImageVector? = null,
    iconDescription: String? = "",
    buttonText: String,
    textStyle: TextStyle? = typography.h4,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.wrapContentHeight().wrapContentWidth()
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.padding(end = Dimens.grid_0_5).size(Dimens.grid_2),
                imageVector = icon,
                contentDescription = iconDescription
            )
        }
        textStyle?.let {
            Text(
                text = buttonText,
                style = it,
            )
        }
    }
}
