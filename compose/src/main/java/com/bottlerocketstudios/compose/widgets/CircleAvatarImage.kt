package com.bottlerocketstudios.compose.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.snippets.snippetDetails.returnMockSnippetDetails
import com.bottlerocketstudios.compose.util.Preview

@Composable
fun CircleAvatarImage(
    imgUri: String?,
    sizeDp: Dp = Dimens.grid_7,
    borderStroke: BorderStroke? = null,
    @StringRes contentDescription: Int,
    @DrawableRes placeholder: Int,
) {
    val modifier = Modifier
        .width(sizeDp)
        .height(sizeDp)
        .clip(CircleShape)

    AsyncImage(
        model = imgUri,
        contentDescription = stringResource(contentDescription),
        modifier = if (borderStroke == null)
            modifier else modifier.border(borderStroke, shape = CircleShape),
        placeholder = painterResource(placeholder),
        contentScale = ContentScale.Crop,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCircleAvatarImage() {
    Preview {
        CircleAvatarImage(
            imgUri = returnMockSnippetDetails().currentUser.value?.avatarUrl,
            contentDescription = R.string.description_avatar,
            placeholder = R.drawable.ic_avatar_placeholder,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCircleAvatarImageWithBorder() {
    Preview {
        CircleAvatarImage(
            imgUri = returnMockSnippetDetails().currentUser.value?.avatarUrl,
            borderStroke = BorderStroke(2.dp, Colors.primary),
            contentDescription = R.string.description_avatar,
            placeholder = R.drawable.ic_avatar_placeholder,
        )
    }
}
