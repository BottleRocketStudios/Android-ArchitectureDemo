package com.bottlerocketstudios.compose.filterdropdown

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.resources.brown_grey
import com.bottlerocketstudios.compose.resources.typography
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewComposable

@Composable
fun FilterDropDownMenu(
    modifier: Modifier = Modifier,
    selectedText: String,
    selectionList: List<String>,
    onFilterSelectionClicked: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Column(modifier = modifier) {
        Card(
            border = BorderStroke(1.dp, brown_grey),
            modifier = Modifier.wrapContentWidth()
        ) {
            Row(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .onGloballyPositioned { coordinates ->
                        // This value is used to assign to the DropDown the same width
                        textFieldSize = coordinates.size.toSize()
                    }
                    .wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.grid_2),
            ) {
                Text(
                    text = selectedText,
                    style = typography.body1,
                    modifier = Modifier.padding(start = Dimens.grid_2_5, top = Dimens.grid_1_5, bottom = Dimens.grid_1_5)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = brown_grey,
                    modifier = Modifier.padding(top = Dimens.grid_1_25, bottom = Dimens.grid_1, end = Dimens.grid_1)
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            selectionList.forEach { label ->
                DropdownMenuItem(onClick = {
                    onFilterSelectionClicked.invoke(label)
                    expanded = !expanded
                }) {
                    Text(text = label, style = typography.body1)
                }
            }
        }
    }
}

@PreviewComposable
@Composable
fun FilterDropDownPreview() {
    Preview {
        FilterDropDownMenu(
            selectedText = "Open",
            selectionList = listOf("Open", "Closed", "Merged"),
            onFilterSelectionClicked = {}
        )
    }
}
