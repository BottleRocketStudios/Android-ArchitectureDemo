package com.bottlerocketstudios.brarchitecture.domain.models

data class SnippetComment(
    val id: Int? = null,
    val created: String? = null,
    val updated: String? = null,
    val content: SnippetCommentContent? = null,
    val user: User? = null,
    val deleted: Boolean? = null,
    val parentId: Int? = null,
    val childrenComments: MutableList<SnippetComment> = mutableListOf(),
    val links: Links? = null,
    val type: String? = null,
    val snippet: Snippet? = null
) : DomainModel

data class SnippetCommentContent(
    val type: String? = null,
    val raw: String? = null,
    val markup: String? = null,
    val html: String? = null
) : DomainModel
