package com.bottlerocketstudios.compose.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.sea_foam
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.launchpad.compose.bold
import com.bottlerocketstudios.launchpad.compose.light
import com.bottlerocketstudios.launchpad.compose.normal

@Composable
fun CardLayout(userRepositoryUiModel: UserRepositoryUiModel, selectItem: (userRepositoryUiModel: UserRepositoryUiModel) -> Unit) {
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
                    tint = sea_foam,
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
                Column(
                    modifier = Modifier
                        .weight(2f)
                ) {
                    Text(
                        userRepositoryUiModel.repo.name ?: "",
                        color = sea_foam,
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
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            top = Dimens.grid_2_5
                        )
                ) {
                    Text(
                        userRepositoryUiModel.formattedLastUpdatedTime.getString(LocalContext.current),
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

@Preview(showSystemUi = true)
@Composable
fun HomeCardPreview() {
    Preview {
        CardLayout(userRepositoryUiModel = testCard1) {}
    }
}
