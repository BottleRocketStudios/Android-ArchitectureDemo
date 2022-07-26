package com.bottlerocketstudios.compose.snippets.snippetDetails.snippetDetailsWidgets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.util.Preview

@Composable
fun SnippetDetailsPrimaryButton(
    icon: ImageVector? = null,
    iconDescription: String? = "",
    buttonText: String,
    buttonPadding: Dp = Dimens.grid_0_25,
    buttonColor: Color = Colors.primary,
    textStyle: TextStyle? = typography.body1,
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.padding(buttonPadding),
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor)
    ) {
        icon?.let {
            Icon(
                modifier = Modifier
                    .padding(end = Dimens.grid_0_5)
                    .size(Dimens.grid_2),
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

@Preview(showBackground = true)
@Composable
fun PreviewSnippetDetailsPrimaryButton() {
    Preview {
        SnippetDetailsPrimaryButton(buttonText = "Button Preview") {
        }
    }
}
