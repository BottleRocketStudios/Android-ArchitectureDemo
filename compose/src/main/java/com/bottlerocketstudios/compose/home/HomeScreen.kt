package com.bottlerocketstudios.compose.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.brown_grey
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewAllDevices
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.launchpad.compose.bold

@Composable
fun HomeScreen(state: HomeScreenState) {
    Column {
        Text(
            text = stringResource(id = R.string.home_repositories),
            style = MaterialTheme.typography.h1.bold(),
            modifier = Modifier
                .padding(
                    start = Dimens.grid_2_5,
                    end = Dimens.grid_1_5,
                    top = Dimens.grid_3_5,
                    bottom = Dimens.grid_1_5
                )
        )
        Box(
            modifier = Modifier
                .padding(
                    bottom = Dimens.grid_1_5,
                    start = Dimens.grid_1_5,
                    end = Dimens.grid_1_5
                )
                .height(Dimens.plane_1)
                .fillMaxWidth()
                .background(color = brown_grey)
        )
        if (state.repositories.value.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Dimens.grid_1_5),
                modifier = Modifier
                    .padding(
                        start = Dimens.grid_1_5,
                        end = Dimens.grid_1_5
                    )
                    .fillMaxSize()
            ) {
                items(
                    items = state.repositories.value,
                    itemContent = { item -> CardLayout(item, state.itemSelected) }
                )
            }
        } else {
            HomeEmptyLayout()
        }
    }
}

@PreviewAllDevices
@Composable
fun HomeScreenPreview() {
    Preview {
        HomeScreen(
            state = HomeScreenState(
                listOf(testCard1, testCard2).asMutableState()
            ) {}
        )
    }
}
