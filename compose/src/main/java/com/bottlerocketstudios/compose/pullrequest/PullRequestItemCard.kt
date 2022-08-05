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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
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

private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f

@Composable
fun PullRequestItemCard(state: PullRequestItemState) {
    Card(
        elevation = Dimens.plane_3,
        modifier = Modifier.wrapContentWidth()
    ) {
        Row(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_repository),
                contentDescription = stringResource(id = R.string.home_repository_icon),
                tint = Colors.tertiary,
                modifier = Modifier
                    .padding(start = 36.dp, end = 14.dp, top = 6.dp)
                    .align(Alignment.Top)
                    .weight(1f)
            )
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .weight(3f)
            ) {
                Row {
                    ResponsiveText(
                        text = state.prName.value,
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(end = 8.dp)
                    )
                }
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(text = state.prState.value, color = greyish_brown, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(60.dp)
                ) {
                    Text(text = state.linesAdded.value, color = greyish_brown, fontSize = 10.sp)
                    Text(text = state.linesRemoved.value, color = greyish_brown, fontSize = 10.sp)
                }
            }
            Text(
                text = state.prCreation.value,
                color = greyish_brown,
                fontSize = 10.sp,
                modifier = Modifier
                    .wrapContentWidth()
                    .weight(2f)
                    .padding(end = 37.dp)
            )
        }
    }
}

@PreviewComposable
@Composable
fun PullRequestsItemPreview() {
    Preview {
        PullRequestItemCard(
            PullRequestItemState(
                prName = "ASAA-19/PR-Screen ASAA-19/PR-Screen ASAA-19/PR-Screen ASAA-19/PR-Screen ASAA-19/PR-Screen".asMutableState(),
                prState = "Open".asMutableState(),
                prCreation = "5 days ago".asMutableState(),
                linesAdded = "0 Lines Added".asMutableState(),
                linesRemoved = "0 Lines Removed".asMutableState()
            )
        )
    }
}

@Composable
fun ResponsiveText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = sea_foam,
    textAlign: TextAlign = TextAlign.Start,
    textStyle: TextStyle,
    targetTextSizeHeight: TextUnit = textStyle.fontSize,
    maxLines: Int = 2,
) {
    var textSize by remember { mutableStateOf(targetTextSizeHeight) }

    Text(
        modifier = modifier,
        text = text,
        color = color,
        textAlign = textAlign,
        fontSize = textSize,
        fontFamily = textStyle.fontFamily,
        fontStyle = textStyle.fontStyle,
        fontWeight = textStyle.fontWeight,
        lineHeight = textStyle.lineHeight,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

            if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                textSize = textSize.times(TEXT_SCALE_REDUCTION_INTERVAL)
            }
        },
    )
}
