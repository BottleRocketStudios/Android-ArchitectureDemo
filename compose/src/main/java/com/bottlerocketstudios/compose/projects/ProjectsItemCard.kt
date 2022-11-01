package com.bottlerocketstudios.compose.projects

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewComposable
import com.bottlerocketstudios.compose.util.ResponsiveText
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.launchpad.compose.bold

@Composable
fun ProjectsItemCard(state: ProjectsItemState) {
    Card(
        elevation = Dimens.plane_3,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.grid_1,
            )
    ) {
        Row {
            Column(
                modifier = Modifier.padding(
                    start = Dimens.grid_4_5,
                    top = Dimens.grid_2,
                    bottom = Dimens.grid_4
                )
            ) {
                ResponsiveText(
                    text = state.name.value,
                    textStyle = typography.h3,
                    modifier = Modifier
                        .padding(end = Dimens.grid_1)
                )
                Text(text = state.key.value, style = typography.body1.bold())
            }
            Column(
                modifier = Modifier.fillMaxSize().padding(
                    top = Dimens.grid_2,
                    end = Dimens.grid_2
                ),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = state.updated.value,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@PreviewComposable
@Composable
private fun PullRequestsItemPreview() {
    Preview {
        ProjectsItemCard(
            state = ProjectsItemState(
                name = "Projecto".asMutableState(),
                key = "Keyto".asMutableState(),
                updated = "77 Days Ago".asMutableState()
            )
        )
    }
}
