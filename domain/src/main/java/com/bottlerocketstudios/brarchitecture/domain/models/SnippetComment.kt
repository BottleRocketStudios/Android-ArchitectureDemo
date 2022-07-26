package com.bottlerocketstudios.brarchitecture.domain.models

class SnippetComment(
    val id: Int?,
    val created: String?,
    val updated: String?,
    val content: SnippetCommentContent?,
    val user: User?,
    val deleted: Boolean?,
    val parentId: Int?,
    val childrenComments: MutableList<SnippetComment> = mutableListOf(),
    val links: Links? = null,
    val type: String?,
    val snippet: Snippet? = null
): DomainModel

data class SnippetCommentContent(
    val type: String?,
    val raw: String?,
    val markup: String?,
    val html: String?
) : DomainModel
