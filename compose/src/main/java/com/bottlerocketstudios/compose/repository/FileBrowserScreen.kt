package com.bottlerocketstudios.compose.repository

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.brown_grey
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewAllDevices
import com.bottlerocketstudios.compose.util.convertToImageBitmap
import com.bottlerocketstudios.launchpad.compose.light
import com.bottlerocketstudios.launchpad.compose.normal

@Composable
fun FileBrowserScreen(state: FileBrowserScreenState) {
    Column(
        modifier = Modifier
            .background(Colors.background)
            .padding(
                start = Dimens.grid_1_5,
                end = Dimens.grid_1_5
            )
    ) {
        Text(
            text = state.path.value,
            style = MaterialTheme.typography.h5.normal(),
            color = Colors.onSurface,
            modifier = Modifier
                .padding(
                    top = Dimens.grid_1
                )
                .wrapContentHeight()
                .fillMaxWidth()
        )
        Divider(
            color = brown_grey,
            modifier = Modifier
                .padding(
                    top = Dimens.grid_1
                )
                .wrapContentHeight()
                .fillMaxWidth()
        )
        RawFileData(byteArray = state.content.value)
    }
}

@Composable
fun RawFileData(byteArray: ByteArray?) {
    byteArray?.let {
        val imageBitmap = byteArray.convertToImageBitmap()
        if (imageBitmap != null) {
            ImageFileLayout(imageBitmap)
        } else {
            TextFileLayout(byteArray.decodeToString())
        }
    }
}

@Composable
fun ImageFileLayout(imageBitmap: ImageBitmap) {
    Image(
        imageBitmap,
        contentDescription = stringResource(id = R.string.file_image_description),
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
fun TextFileLayout(rawString: String) {
    val scroll = rememberScrollState(0)
    Text(
        text = rawString,
        style = MaterialTheme.typography.h5.light(),
        color = Colors.onBackground,
        modifier = Modifier
            .padding(
                top = Dimens.grid_1
            )
            .fillMaxWidth()
            .verticalScroll(scroll)
    )
}

@PreviewAllDevices
@Composable
fun FileBrowserScreenPreview() {
    Preview {
        FileBrowserScreen(
            state = fileBrowserMockData
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun FileBrowserDarkPreview() {
    Preview(darkTheme = true) {
        FileBrowserScreen(state = fileBrowserMockData)
    }
}

