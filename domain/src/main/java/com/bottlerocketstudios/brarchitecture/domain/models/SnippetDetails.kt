package com.bottlerocketstudios.brarchitecture.domain.models

data class SnippetDetails(
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
) : DomainModel
