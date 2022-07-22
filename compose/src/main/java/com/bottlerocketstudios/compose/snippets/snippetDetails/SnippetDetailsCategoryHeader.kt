package com.bottlerocketstudios.compose.snippets.snippetDetails

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.typography

@Composable
fun CategoryHeader(header: String) {
    Text(
        text = header,
        style = typography.h2.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(top = Dimens.grid_1_5, bottom = Dimens.grid_0_25)
    )
}
