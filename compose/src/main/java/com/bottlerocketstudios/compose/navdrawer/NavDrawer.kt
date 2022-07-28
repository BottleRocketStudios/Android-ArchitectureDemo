package com.bottlerocketstudios.compose.navdrawer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.compose.widgets.OutlinedSurfaceButton

// TODO - Possibly move this into LaunchPad Library
@Composable
fun ColumnScope.NavDrawer(state: NavDrawerState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.grid_2)
            .background(MaterialTheme.colors.secondary)
            .padding(
                top = Dimens.grid_5,
                start = Dimens.grid_3,
                bottom = Dimens.grid_4
            )
    ) {
        // TODO pull header out into content slot to make easier for re-use
        AsyncImage(
            model = state.avatarUrl.value,
            contentDescription = stringResource(id = R.string.profile_image_description),
            modifier = Modifier
                .size(66.dp)
                .background(MaterialTheme.colors.onSecondary)
        )
        Text(
            text = state.displayName.value,
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .padding(top = Dimens.grid_1)
        )
        Text(
            text = state.username.value,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .padding(top = Dimens.grid_1)
        )
    }

    for (item in state.items.value) {
        NavDrawerItem(state = item)
    }

    Spacer(Modifier.weight(1f))
    // Pull this out into a "footer" content slot.
    OutlinedSurfaceButton(
        buttonText = stringResource(id = R.string.dev_options_button),
        forceCaps = true,
        onClick = state.devOptionsListener,
        // onClick = { navController.navigate(Routes.DevOptions) },
        modifier = Modifier
            .padding(
                top = Dimens.grid_4,
                bottom = Dimens.grid_2
            )
            .align(Alignment.CenterHorizontally)
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    Preview {
        Scaffold(
            scaffoldState = ScaffoldState(
                drawerState = DrawerState(
                    initialValue = DrawerValue.Open
                ),
                snackbarHostState = SnackbarHostState()
            ),
            topBar = {},
            drawerContent = {
                NavDrawer(
                    state = NavDrawerState(
                        avatarUrl = "https://placekitten.com/66/66".asMutableState(),
                        displayName = "Cool Cat".asMutableState(),
                        username = "Claws_N_Paws".asMutableState(),
                        items = generateItemListPreview().asMutableState(),
                        devOptionsListener = {},
                    )
                )
            }
        ) {}
    }
}

@Composable
fun generateItemListPreview(): List<NavItemState> {
    val itemList by remember {
        mutableStateOf(
            listOf(
                NavItemState(
                    icon = R.drawable.ic_hide,
                    itemText = R.string.login_id_hint,
                    selected = true
                ) {},
                NavItemState(
                    icon = R.drawable.ic_hide,
                    itemText = R.string.login_id_hint,
                    selected = false
                ) {},
                NavItemState(
                    icon = R.drawable.ic_hide,
                    itemText = R.string.login_id_hint,
                    selected = false
                ) {}
            )
        )
    }
    return itemList
}
