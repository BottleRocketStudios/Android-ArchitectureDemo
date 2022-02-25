package com.bottlerocketstudios.compose.widgets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme

@Composable
fun PrimaryButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .padding(ArchitectureDemoTheme.dimens.grid_1),
        shape = RoundedCornerShape(ArchitectureDemoTheme.dimens.grid_1),
        contentPadding = PaddingValues(ArchitectureDemoTheme.dimens.grid_1_5)
    ) {
        Text(
            text = buttonText,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPrimaryButton() {
    ArchitectureDemoTheme {
        PrimaryButton(
            buttonText = "Hello World",
            onClick = {  },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
