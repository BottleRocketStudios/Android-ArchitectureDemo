package com.bottlerocketstudios.compose.filterdropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.bottlerocketstudios.compose.util.Preview

@Composable
fun PullRequestFilterBy(state: FilterDropDownState) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(state.selectionList.first()) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Column {
        OutlinedTextField(
            value = selectedText,
            enabled = false,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .widthIn(max = 111.dp)
                .heightIn(max = 40.dp)
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            trailingIcon = {
                Icon(
                    if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    "contentDescription",
                    Modifier
                        .clickable { expanded = !expanded }
                        .size(width = 24.dp, height = 24.dp)
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            state.selectionList.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label
                    state.onFilterSelectionClicked.invoke(selectedText)
                    expanded = !expanded
                }) {
                    Text(text = label)
                }
            }
        }
    }
}


@Preview
@Composable
fun FilterDropDownPreview() {
    Preview {
        PullRequestFilterBy(
            FilterDropDownState(
                selectionList = listOf("Open", "Closed", "Merged"),
                onFilterSelectionClicked = {}
            )
        )
    }
}
