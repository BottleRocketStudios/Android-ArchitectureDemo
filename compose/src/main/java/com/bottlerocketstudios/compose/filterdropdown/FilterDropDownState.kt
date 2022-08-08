package com.bottlerocketstudios.compose.filterdropdown

data class FilterDropDownState(
    val selectionList: List<String>,
    val onFilterSelectionClicked: (String) -> Unit,
)
