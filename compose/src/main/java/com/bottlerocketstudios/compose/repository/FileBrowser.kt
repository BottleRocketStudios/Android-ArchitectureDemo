package com.bottlerocketstudios.compose.repository

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.brown_grey
import com.bottlerocketstudios.compose.resources.greyish_brown
import com.bottlerocketstudios.compose.resources.light
import com.bottlerocketstudios.compose.resources.normal
import com.bottlerocketstudios.compose.util.Preview

data class FileBrowserScreenState(
    val path: State<String>,
    val content: State<String>
)

@Composable
fun FileBrowserScreen(state: FileBrowserScreenState) {
    val scroll = rememberScrollState(0)
    Column(
        modifier = Modifier
            .padding(
                start = Dimens.grid_1_5,
                end = Dimens.grid_1_5
            )
    ) {
        Text(
            text = state.path.value,
            style = MaterialTheme.typography.h5.normal(),
            color = greyish_brown,
            modifier = Modifier
                .padding(
                    top = Dimens.grid_1
                )
                .wrapContentHeight()
                .fillMaxWidth()
        )
        Divider(
            color = brown_grey,
            thickness = Dimens.plane_1,
            modifier = Modifier
                .padding(
                    top = Dimens.grid_1
                )
                .wrapContentHeight()
                .fillMaxWidth()
        )
        Text(
            text = state.content.value,
            style = MaterialTheme.typography.h5.light(),
            modifier = Modifier
                .padding(
                    top = Dimens.grid_1
                )
                .fillMaxWidth()
                .verticalScroll(scroll)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun FileBrowserScreenPreview() {
    Preview {
        FileBrowserScreen(
            state = fileBrowserMockData
        )
    }
}
