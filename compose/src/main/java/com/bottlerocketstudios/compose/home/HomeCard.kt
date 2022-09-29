package com.bottlerocketstudios.compose.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.brown_grey
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewComposable
import com.bottlerocketstudios.compose.util.ResponsiveText
import com.bottlerocketstudios.launchpad.compose.bold
import com.bottlerocketstudios.launchpad.compose.light
import com.bottlerocketstudios.launchpad.compose.normal

@Composable
fun RepositoryCardLayout(userRepositoryUiModel: UserRepositoryUiModel, selectItem: (userRepositoryUiModel: UserRepositoryUiModel) -> Unit) {
    Card(
        elevation = Dimens.plane_3,
        modifier = Modifier
            .clickable {
                selectItem(userRepositoryUiModel)
            }
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = Dimens.grid_1,
                    end = Dimens.grid_1,
                    top = Dimens.grid_1,
                    bottom = Dimens.grid_1
                )
                .wrapContentHeight(align = Alignment.CenterVertically)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .wrapContentWidth()

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_repository),
                    contentDescription = stringResource(id = R.string.home_repository_icon),
                    tint = Colors.tertiary,
                    modifier = Modifier
                        .padding(
                            all = Dimens.grid_2
                        )
                )
            }
            Row(
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.Top)
            ) {
                RepoDescriptionComponent(userRepositoryUiModel = userRepositoryUiModel, Modifier.weight(2f))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            top = Dimens.grid_2_5
                        )
                ) {
                    Text(
                        userRepositoryUiModel.formattedLastUpdatedTime.getString(),
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

@Composable
fun PullRequestCardLayout(userPullRequestUIModel: UserPullRequestUIModel) {
    userPullRequestUIModel.pullRequest.title
    Card(
        elevation = Dimens.plane_3,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(top = Dimens.grid_2, bottom = Dimens.grid_2)) {
            @Suppress("MagicNumber")
            Column(
                modifier = Modifier.padding(start = Dimens.grid_2_5, end = Dimens.grid_2_5)
            ) {
                Row {
                    ResponsiveText(
                        text = userPullRequestUIModel.pullRequest.title,
                        textStyle = typography.h3,
                        modifier = Modifier
                            .padding(end = Dimens.grid_1)
                            .weight(3f)
                    )
                    Text(
                        text = userPullRequestUIModel.formattedLastUpdatedTime.getString(),
                        fontSize = 10.sp
                    )
                }
                Row(
                    modifier = Modifier.padding(top = Dimens.grid_0_5),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.grid_2)
                ) {
                    Text(
                        text = userPullRequestUIModel.pullRequest.source,
                        style = typography.body1,
                        color = Colors.onBackground,
                        modifier = Modifier
                            .background(color = brown_grey, shape = RoundedCornerShape(Dimens.grid_0_5))
                            .padding(Dimens.grid_0_75)
                    )
                    Text(text = "→", style = typography.body1)
                    Text(
                        text = userPullRequestUIModel.pullRequest.destination,
                        style = typography.body1,
                        color = Colors.onBackground,
                        modifier = Modifier
                            .background(color = brown_grey, shape = RoundedCornerShape(Dimens.grid_0_5))
                            .padding(Dimens.grid_0_75),
                        textAlign = TextAlign.End
                    )
                }
                Row(Modifier.fillMaxWidth().padding(top = Dimens.grid_1)) {
                    Text(text = userPullRequestUIModel.pullRequest.author, style = typography.body1.bold())
                    Text(text = userPullRequestUIModel.pullRequest.reviewers, style = typography.body1, modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontStyle = FontStyle.Italic)
                }
            }
        }
    }
}

@Composable
fun RepoDescriptionComponent(userRepositoryUiModel: UserRepositoryUiModel, modifier: Modifier) {

    Column(
        modifier = modifier
    ) {
        Text(
            userRepositoryUiModel.repo.name ?: "",
            color = Colors.tertiary,
            style = MaterialTheme.typography.h3.bold(),
            modifier = Modifier
                .padding(
                    top = Dimens.grid_1
                )
                .wrapContentHeight()
                .fillMaxWidth()
        )
        Text(
            userRepositoryUiModel.repo.owner?.displayName ?: "",
            style = MaterialTheme.typography.h5.normal(),
            modifier = Modifier
                .padding(
                    top = Dimens.grid_0_5
                )
                .wrapContentHeight()
                .fillMaxWidth()
        )
        Text(
            userRepositoryUiModel.repo.description ?: "",
            style = MaterialTheme.typography.h6.light(),
            modifier = Modifier
                .padding(
                    top = Dimens.grid_0_5,
                    bottom = Dimens.grid_1
                )
                .wrapContentHeight()
                .fillMaxWidth()
        )
    }
}

@PreviewComposable
@Composable
private fun HomeCardPreview() {
    Preview {
        RepositoryCardLayout(userRepositoryUiModel = testCard1) {}
    }
}
