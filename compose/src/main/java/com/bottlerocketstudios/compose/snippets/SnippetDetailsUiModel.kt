package com.bottlerocketstudios.compose.snippets

import com.bottlerocketstudios.brarchitecture.domain.models.Links
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.brarchitecture.domain.models.User

data class SnippetDetailsUiModel(
    val id: String? = null,
    val title: String? = null,
    val createdMessage: String? = null,
    val updatedMessage: String? = null,
    val isPrivate: Boolean? = null,
    val files: List<SnippetDetailsFile?>? = null,
    val owner: User? = null,
    val creator: User? = null,
    val links: Links? = null
)
