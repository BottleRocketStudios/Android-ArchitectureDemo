package com.bottlerocketstudios.compose.snippets

import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.brarchitecture.domain.models.User
import java.time.ZonedDateTime

data class SnippetDetailsUiModel(
    val id: String? = null,
    val title: String? = null,
    val created: ZonedDateTime? = null,
    val updated: ZonedDateTime? = null,
    val isPrivate: Boolean? = null,
    val files: List<SnippetDetailsFile?>? = null,
    val owner: User? = null,
    val creator: User? = null,
)
