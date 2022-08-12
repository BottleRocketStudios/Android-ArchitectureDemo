package com.bottlerocketstudios.compose.snippets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewComposable
import com.bottlerocketstudios.launchpad.compose.bold
import com.bottlerocketstudios.launchpad.compose.light
import com.bottlerocketstudios.launchpad.compose.normal

@Composable
fun SnippetItem(snippet: SnippetUiModel, onClick: (SnippetUiModel) -> Unit) {
    Card(
        elevation = Dimens.plane_3,
        modifier = Modifier.wrapContentHeight()
            .clickable {
                onClick(snippet)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(all = Dimens.grid_1)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_snippet_blue),
                contentDescription = stringResource(id = R.string.snippet_icon),
                tint = ArchitectureDemoTheme.colors.tertiary,
                modifier = Modifier.padding(all = Dimens.grid_2)
            )
            Row(
                modifier = Modifier.wrapContentHeight(align = Alignment.Top)
            ) {
                Column(
                    modifier = Modifier.weight(2f)
                ) {
                    Text(
                        snippet.title,
                        color = Colors.tertiary,
                        style = MaterialTheme.typography.h3.bold(),
                        modifier = Modifier
                            .padding(top = Dimens.grid_1)
                            .wrapContentHeight()
                            .fillMaxWidth()
                    )
                    Text(
                        snippet.userName,
                        style = MaterialTheme.typography.h5.normal(),
                        modifier = Modifier
                            .padding(top = Dimens.grid_0_5)
                            .wrapContentHeight()
                            .fillMaxWidth()
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = Dimens.grid_2_5)
                ) {
                    Text(
                        snippet.formattedLastUpdatedTime.getString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6.light(),
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@PreviewComposable
@Composable
fun SnippetItemPreview() {
    Preview {
        SnippetItem(snippet = mockSnippet1, onClick = {})
    }
}
