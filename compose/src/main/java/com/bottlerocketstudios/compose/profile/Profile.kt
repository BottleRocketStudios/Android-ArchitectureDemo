package com.bottlerocketstudios.compose.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.bold
import com.bottlerocketstudios.compose.resources.br_red
import com.bottlerocketstudios.compose.resources.greyish_brown
import com.bottlerocketstudios.compose.resources.normal
import com.bottlerocketstudios.compose.resources.tertiary
import com.bottlerocketstudios.compose.util.Preview

data class ProfileScreenState(
    val avatarUrl: State<String>,
    val displayName: State<String>,
    val nickname: State<String>,
    val onEditClicked: () -> Unit,
    val onLogoutClicked: () -> Unit,
)

@Composable
fun ProfileScreen(state: ProfileScreenState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        AsyncImage(
            model = state.avatarUrl.value,
            contentDescription = stringResource(id = R.string.profile_image_description),
            modifier = Modifier
                .padding(
                    top = 142.dp
                )
                .height(Dimens.grid_12_5)
        )
        Text(
            state.displayName.value,
            color = tertiary,
            style = MaterialTheme.typography.h3.bold(),
            modifier = Modifier
                .padding(
                    top = Dimens.grid_2
                )
                .wrapContentSize()
        )
        Text(
            state.nickname.value,
            color = greyish_brown,
            style = MaterialTheme.typography.h4.normal(),
            modifier = Modifier
                .padding(
                    top = Dimens.grid_0_5
                )
                .wrapContentSize()
        )
        OutlinedButton(
            onClick = { state.onEditClicked() },
            border = BorderStroke(1.dp, br_red),
            shape = RoundedCornerShape(Dimens.grid_1_5),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = br_red),
            modifier = Modifier
                .padding(
                    top = Dimens.grid_5_5
                )
                .defaultMinSize(
                    minWidth = Dimens.grid_12_5
                )
        ) {
            Text(
                stringResource(id = R.string.profile_edit).uppercase(),
                color = br_red,
                style = MaterialTheme.typography.h3.bold(),
                modifier = Modifier
                    .wrapContentSize()
            )
        }
        OutlinedButton(
            onClick = { state.onLogoutClicked() },
            border = BorderStroke(1.dp, Color.Transparent),
            shape = RoundedCornerShape(Dimens.grid_1_5),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = greyish_brown),
            modifier = Modifier
                .padding(
                    top = Dimens.grid_0_5
                )
                .defaultMinSize(
                    minWidth = Dimens.grid_12_5
                )
        ) {
            Text(
                stringResource(id = R.string.profile_logout),
                color = greyish_brown,
                style = MaterialTheme.typography.h3.bold(),
                modifier = Modifier
                    .wrapContentSize()
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    Preview {
        ProfileScreen(
            state = ProfileMockData
        )
    }
}
