package com.bottlerocketstudios.compose.widgets

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.util.Preview

@Preview(showBackground = true)
@Composable
private fun PreviewAppBar() {
    Preview {
        AppBar(
            title = "Title",
            navIcon = Icons.Default.ArrowBack,
            onNavClicked = {}
        )
    }
}

@Composable
fun AppBar(
    title: String,
    navIcon: ImageVector,
    onNavClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.h1
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavClicked) {
                Icon(navIcon, contentDescription = "")
            }
        },
    )
}
