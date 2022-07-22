package com.bottlerocketstudios.compose.navdrawer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.launchpad.compose.bold
import kotlinx.coroutines.launch

@Composable
fun NavDrawerItem(
    state: NavItemState
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(Dimens.minimum_touch_target)
            .fillMaxWidth()
            .clickable {
                coroutineScope.launch {
                    state.onClick()
                }
            }
            .padding(
                start = Dimens.grid_4
            )
    ) {
        Icon(
            painter = painterResource(id = state.icon),
            contentDescription = null,
            tint = if (state.selected) MaterialTheme.colors.error else LocalContentColor.current,
        )

        Text(
            text = stringResource(id = state.itemText),
            style = MaterialTheme.typography.h4.bold(),
            color = if (state.selected) MaterialTheme.colors.error else MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(start = Dimens.grid_3)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NavItemPreview() {
    Preview {
        Column {
            NavDrawerItem(
                NavItemState(
                    icon = R.drawable.ic_hide,
                    itemText = R.string.login_id_hint,
                    selected = false
                ) {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavItemSelectedPreview() {
    Preview {
        Column {
            NavDrawerItem(
                NavItemState(
                    icon = R.drawable.ic_hide,
                    itemText = R.string.login_id_hint,
                    selected = true
                ) {}
            )
        }
    }
}


