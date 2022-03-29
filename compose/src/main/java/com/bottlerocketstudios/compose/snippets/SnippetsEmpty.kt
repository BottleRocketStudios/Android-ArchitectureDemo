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
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.resources.bold
import com.bottlerocketstudios.compose.util.Preview

@Composable
fun SnippetsEmptyLayout() {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ){
        Text(
            text = stringResource(R.string.no_snippets),
            color = ArchitectureDemoTheme.colors.tertiary,
            style = MaterialTheme.typography.h1.bold()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeEmptyPreview() {
    Preview {
        SnippetsEmptyLayout()
    }
}
