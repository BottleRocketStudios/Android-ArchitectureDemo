package com.bottlerocketstudios.compose.pullrequest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.greyish_brown
import com.bottlerocketstudios.compose.resources.sea_foam
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewComposable
import com.bottlerocketstudios.compose.util.asMutableState

@Composable
fun PullRequestItemCard(state: PullRequestItemState) {
    Card(
        elevation = Dimens.plane_3,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.ic_repository),
                contentDescription = stringResource(id = R.string.home_repository_icon),
                tint = Colors.tertiary,
                modifier = Modifier
                    .padding(start = 20.dp, end = 14.dp, top = 6.dp)
                    .align(Alignment.Top)
            )
            Column {
                Row(
                    modifier = Modifier
                        .padding(end = 37.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = state.prName.value, color = sea_foam, fontSize = 16.sp)
                    Text(text = state.prCreation.value, color = greyish_brown, fontSize = 10.sp)
                }

                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(text = state.prState.value, color = greyish_brown, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp, end = 37.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(60.dp)
                ) {
                    Text(text = state.linesAdded.value.toString(), color = greyish_brown, fontSize = 10.sp)
                    Text(text = state.linesRemoved.value.toString(), color = greyish_brown, fontSize = 10.sp)
                }
            }
        }
    }
}

@PreviewComposable
@Composable
fun PullRequestsItemPreview() {
    Preview {
        PullRequestItemCard(
            PullRequestItemState(
                prName = "ASAA-19/PR-Screen".asMutableState(),
                prState = "Open".asMutableState(),
                prCreation = "5 days ago".asMutableState(),
                linesAdded = 5.asMutableState(),
                linesRemoved = 10.asMutableState()
            )
        )
    }
}
