package com.bottlerocketstudios.brarchitecture.domain.models

class SnippetComment(
    val id: Long?,
    val created: String?,
    val updated: String?,
    val content: SnippetCommentContent?,
    val user: User?,
    val deleted: Boolean?,
    val parentId: Long?,
    val childrenComments: MutableList<SnippetComment?>,
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
