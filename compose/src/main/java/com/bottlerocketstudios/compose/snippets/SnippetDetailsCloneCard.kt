package com.bottlerocketstudios.compose.snippets

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography

@Composable
fun SnippetDetailsCloneCard(
    type: String,
    link: String,
    copyClick: () -> Unit
) {
    Card(
        elevation = Dimens.plane_3,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.grid_0_5)
    ) {
        val clipboardManager = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(type, link)
        Column(modifier = Modifier.padding(Dimens.grid_2)) {
            Text(
                text = type,
                style = typography.h3,
                modifier = Modifier.padding(end = Dimens.grid_1)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.wrapContentHeight()
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy Icon",
                    modifier = Modifier
                        .clickable {
                            clipboardManager.setPrimaryClip(clipData)
                        }
                )
                OutlinedTextField(
                    value = link,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    textStyle = typography.h4,
                )
            }
        }
    }
}
