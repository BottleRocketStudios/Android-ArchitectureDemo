package com.bottlerocketstudios.compose.snippets.snippetDetails

import com.bottlerocketstudios.brarchitecture.domain.models.DomainModel
import com.bottlerocketstudios.brarchitecture.domain.models.Links
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFileLinks
import com.bottlerocketstudios.brarchitecture.domain.models.User

data class SnippetDetailsUiModel(
    val id: String? = "",
    val title: String? = "",
    val createdMessage: String? = "",
    val updatedMessage: String? = "",
    val isPrivate: Boolean? = null,
    val files: List<SnippetDetailsFileLinks>? = null,
    val owner: User? = null,
    val creator: User? = null,
    val links: Links? = null,
    val httpsCloneLink: String? = "",
    val sshCloneLink: String? = "",
): DomainModel
