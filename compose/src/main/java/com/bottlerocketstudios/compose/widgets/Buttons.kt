package com.bottlerocketstudios.compose.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    forceCaps: Boolean = false,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.grid_1),
        contentPadding = PaddingValues(Dimens.grid_1_5),
        enabled = enabled,
    ) {
        Text(
            text = if (forceCaps) buttonText.uppercase() else buttonText,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPrimaryButton() {
    Preview {
        PrimaryButton(
            buttonText = "Hello World",
            onClick = { },
            modifier = Modifier
                .padding(Dimens.grid_1)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPrimaryButtonDisabled() {
    Preview {
        PrimaryButton(
            buttonText = "Hello World",
            onClick = { },
            enabled = false,
            modifier = Modifier
                .padding(Dimens.grid_1)
                .fillMaxWidth()
        )
    }
}

@Composable
fun SurfaceButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    forceCaps: Boolean = false,
    onClick: () -> Unit,
) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            primary = MaterialTheme.colors.surface,
            onPrimary = MaterialTheme.colors.primary
        )
    ) {
        Button(
            onClick = { onClick() },
            modifier = modifier,
            elevation = null,
            shape = RoundedCornerShape(Dimens.grid_1),
            contentPadding = PaddingValues(Dimens.grid_1_5)
        ) {
            Text(
                text = if (forceCaps) buttonText.uppercase() else buttonText,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSurfaceButton() {
    Preview {
        SurfaceButton(
            buttonText = "Hello World",
            onClick = { },
            modifier = Modifier
                .padding(Dimens.grid_1)
                .fillMaxWidth()
        )
    }
}

@Composable
fun OutlinedSurfaceButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    forceCaps: Boolean = false,
    onClick: () -> Unit
) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            primary = MaterialTheme.colors.surface,
            onPrimary = MaterialTheme.colors.primary
        )
    ) {
        Button(
            onClick = { onClick() },
            modifier = modifier,
            border = BorderStroke(Dimens.grid_0_25, MaterialTheme.colors.onPrimary),
            shape = RoundedCornerShape(Dimens.grid_1),
            contentPadding = PaddingValues(Dimens.grid_1_5)
        ) {
            Text(
                text = if (forceCaps) buttonText.uppercase() else buttonText,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewOutlineSurfaceButton() {
    Preview {
        OutlinedSurfaceButton(
            buttonText = "Hello World",
            onClick = { },
            modifier = Modifier
                .padding(Dimens.grid_1)
                .fillMaxWidth()
        )
    }
}
