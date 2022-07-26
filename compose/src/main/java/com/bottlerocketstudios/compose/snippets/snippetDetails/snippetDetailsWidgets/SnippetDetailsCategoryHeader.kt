package com.bottlerocketstudios.compose.snippets.snippetDetails.snippetDetailsWidgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.util.Preview

@Composable
fun CategoryHeader(
    header: String,
    style: TextStyle = typography.h2.copy(fontWeight = FontWeight.Bold),
    modifier: Modifier = Modifier
        .padding(horizontal = Dimens.grid_2)
        .padding(top = Dimens.grid_1_5, bottom = Dimens.grid_0_25)
) {
    Text(
        text = header,
        style = style,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryHeader() {
    Preview {
        CategoryHeader(header = "Category Header")
    }
}
