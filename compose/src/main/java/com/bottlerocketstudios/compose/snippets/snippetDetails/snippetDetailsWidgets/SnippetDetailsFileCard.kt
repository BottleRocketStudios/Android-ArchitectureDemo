package com.bottlerocketstudios.compose.snippets.snippetDetails.snippetDetailsWidgets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.snippets.snippetDetails.returnMockSnippetDetails
import com.bottlerocketstudios.compose.util.convertToImageBitmap
import com.bottlerocketstudios.compose.widgets.IconText
import com.bottlerocketstudios.launchpad.compose.light
import com.bottlerocketstudios.launchpad.compose.normal

@Composable
fun SnippetDetailsFilesCard(file: SnippetDetailsFile?) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        elevation = Dimens.plane_3,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = Dimens.grid_1, horizontal = Dimens.grid_2)
    ) {
        Column(
            modifier = Modifier.animateContentSize(tween(1000))
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        vertical = Dimens.grid_1,
                        horizontal = Dimens.grid_2
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconText(
                    iconRes = R.drawable.ic_file,
                    iconColor = Colors.tertiary,
                    text = file?.fileName ?: "",
                    style = typography.h4.normal()
                )
                OutlinedButton(
                    onClick = { expanded = !expanded },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Colors.surface),
                    elevation = null
                ) {
                    Text(
                        text = stringResource(
                            id = if (expanded)
                                R.string.button_file_close else R.string.button_file_raw
                        ),
                        style = typography.body1.copy(color = Colors.onSurface)
                    )
                }
            }

            if (expanded) {
                file?.rawFile?.let { byteArray ->
                    Divider(
                        color = Colors.onSurface,
                        modifier = Modifier
                            .padding(horizontal = Dimens.grid_2)
                            .wrapContentHeight()
                            .fillMaxWidth()
                    )
                    RawFileData(byteArray = byteArray)
                }
            }
        }
    }
}

@Composable
fun RawFileData(byteArray: ByteArray) {
    val imageBitmap = byteArray.convertToImageBitmap()
    if (imageBitmap != null) {
        Image(
            imageBitmap,
            contentDescription = stringResource(id = R.string.file_image_description),
            modifier = Modifier.padding(Dimens.grid_2)
        )
    } else {
        Text(
            text = byteArray.decodeToString(),
            style = MaterialTheme.typography.h5.light(),
            modifier = Modifier
                .padding(Dimens.grid_2)
                .fillMaxWidth()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewFilesCard() {
    SnippetDetailsFilesCard(
        file = returnMockSnippetDetails().files.value[0]
    )
}
