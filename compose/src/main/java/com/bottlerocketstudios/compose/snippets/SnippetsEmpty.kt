package com.bottlerocketstudios.compose.snippets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewAll
import com.bottlerocketstudios.launchpad.compose.bold

@Composable
fun SnippetsEmptyLayout() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.no_snippets),
            color = ArchitectureDemoTheme.colors.tertiary,
            style = MaterialTheme.typography.h1.bold()
        )
    }
}

@PreviewAll
@Composable
private fun HomeEmptyPreview() {
    Preview {
        SnippetsEmptyLayout()
    }
}
