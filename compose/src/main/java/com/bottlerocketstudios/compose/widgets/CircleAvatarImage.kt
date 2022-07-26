package com.bottlerocketstudios.compose.widgets

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.snippets.snippetDetails.returnMockSnippetDetails
import com.bottlerocketstudios.compose.util.Preview

@Composable
fun CircleAvatarImage(
    imgUri: String?,
    sizeDp: Dp = Dimens.grid_7,
) {
    AsyncImage(
        model = imgUri,
        contentDescription = stringResource(R.string.description_avatar),
        modifier = Modifier
            .padding(start = Dimens.grid_2)
            .width(sizeDp)
            .height(sizeDp)
            .clip(CircleShape),
        placeholder = painterResource(R.drawable.ic_avatar_placeholder),
        contentScale = ContentScale.Crop,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCircleAvatarImage() {
    Preview {
        CircleAvatarImage(imgUri = returnMockSnippetDetails().currentUser.value?.avatarUrl)
    }
}
