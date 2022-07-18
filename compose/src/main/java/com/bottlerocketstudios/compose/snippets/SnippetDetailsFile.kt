package com.bottlerocketstudios.compose.snippets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.widgets.IconText
import com.bottlerocketstudios.launchpad.compose.normal

@Composable
fun SnippetDetailsFilesCard(
    file: SnippetDetailsFile?,
    onRawClick: () -> Unit
) {
    Card(
        elevation = Dimens.plane_3,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.grid_1)
            .wrapContentHeight()
    ) {
        Row(
            Modifier.padding(Dimens.grid_1),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconText(
                iconRes = R.drawable.ic_folders,
                iconColor = Colors.tertiary,
                text = file?.fileName ?: "",
                style = typography.h4.normal()
            )
            OutlinedButton(
                onClick = { onRawClick() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Colors.surface),
                elevation = null
            ) {
                Text(
                    text = "Raw",
                    style = typography.body1.copy(color = Colors.onSurface)
                )
            }
        }
    }
}
