package com.bottlerocketstudios.compose.snippets.snippetDetails.snippetDetailsWidgets

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.launchpad.compose.bold

@Composable
fun SnippetDetailsCloneCard(type: String, link: String) {
    Card(
        elevation = Dimens.plane_3,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.grid_2,
                vertical = Dimens.grid_1
            )
    ) {
        val clipboardManager = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(type, link)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(Dimens.grid_1)
                .wrapContentSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(end = Dimens.grid_1)
            ) {
                Text(
                    text = type,
                    style = typography.h3.bold().copy(color = Colors.tertiary),
                )
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = stringResource(id = R.string.description_copy_icon),
                    modifier = Modifier
                        .clickable {
                            clipboardManager.setPrimaryClip(clipData)
                        }
                )
            }
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

@Preview(showBackground = true)
@Composable
fun PreviewCloneCard() {
    Preview {
        Column {
            SnippetDetailsCloneCard(
                type = "HTTP",
                link = "https://username@bitbucket.org/snippets/userName/zEqaLk/clone-card_preview"
            )
            SnippetDetailsCloneCard(
                type = "SSH",
                link = "git@bitbucket.org:snippets/userName/zEqaLk/clone-card_preview.git"
            )
        }
    }
}
