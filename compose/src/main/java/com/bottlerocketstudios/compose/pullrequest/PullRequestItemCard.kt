package com.bottlerocketstudios.compose.pullrequest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewComposable
import com.bottlerocketstudios.compose.util.ResponsiveText
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.launchpad.compose.bold

@Composable
fun PullRequestItemCard(state: PullRequestItemState) {
    Card(
        elevation = Dimens.plane_3,
        modifier = Modifier.wrapContentWidth()
    ) {
        Row(modifier = Modifier.padding(top = Dimens.grid_2, bottom = Dimens.grid_2)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_repository),
                contentDescription = stringResource(id = R.string.home_repository_icon),
                tint = Colors.tertiary,
                modifier = Modifier
                    .padding(start = Dimens.grid_4_5, end = Dimens.grid_1_75, top = Dimens.grid_0_75)
                    .align(Alignment.Top)
                    .weight(1f)
            )
            @Suppress("MagicNumber")
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .weight(3f)
            ) {
                Row {
                    ResponsiveText(
                        text = state.prName.value,
                        textStyle = typography.h3,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(end = Dimens.grid_1)
                    )
                }
                Row(modifier = Modifier.padding(top = Dimens.grid_0_5)) {
                    Text(text = state.prState.value, style = typography.body1.bold())
                }
                Row(
                    modifier = Modifier.padding(top = Dimens.grid_1, bottom = Dimens.grid_1),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.grid_7)
                ) {
                    Text(text = state.linesAdded.value, style = typography.body1)
                    Text(text = state.linesRemoved.value, style = typography.body1)
                }
            }
            Text(
                text = state.prCreation.value,
                fontSize = 10.sp,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(end = Dimens.grid_4_5)
            )
        }
    }
}

@PreviewComposable
@Composable
private fun PullRequestsItemPreview() {
    Preview {
        PullRequestItemCard(
            PullRequestItemState(
                prName = "ASAA-19/PR-Screen ASAA-19/PR-Screen ASAA-19/PR-Screen ASAA-19/PR-Screen ASAA-19/PR-Screen".asMutableState(),
                prState = "Open".asMutableState(),
                prCreation = "5 days ago".asMutableState(),
                linesAdded = "0 Lines Added".asMutableState(),
                linesRemoved = "0 Lines Removed".asMutableState(),
                author = "Arthur Mogran".asMutableState(),
                source = "Branch".asMutableState(),
                destination = "master".asMutableState(),
                reviewers = "No Reviewers".asMutableState(),
            )
        )
    }
}
