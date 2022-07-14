package com.bottlerocketstudios.compose.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview

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

@Preview(showBackground = true)
@Composable
private fun PreviewPrimaryButton() {
    Preview {
        IconText(
            modifier = Modifier
                .padding(Dimens.grid_1)
                .fillMaxWidth(),
            iconRes = R.drawable.ic_folders,
            iconColor = Colors.tertiary,
            text = "Hello World",
            style = MaterialTheme.typography.h4,
        )
    }
}
