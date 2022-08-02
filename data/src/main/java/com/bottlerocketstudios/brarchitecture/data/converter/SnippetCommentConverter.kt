package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.SnippetCommentContentDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetCommentDto
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetCommentContent
import com.bottlerocketstudios.brarchitecture.domain.utils.convertToTimeAgoMessage

fun SnippetCommentDto.convertToComment() = SnippetComment(
    id = id,
    created = created?.convertToTimeAgoMessage(),
    updated = updated?.convertToTimeAgoMessage(),
    content = content?.convertToCommentContent(),
    user = user?.convertToUser(),
    deleted = deleted,
    parentId = parent?.id,
    childrenComments = mutableListOf<SnippetComment>(),
    links = links?.convertToLinks(),
    type = type,
    snippet = snippet?.convertToSnippet()
)

fun SnippetCommentContentDto.convertToCommentContent() = SnippetCommentContent(
    type = type,
    raw = raw,
    markup = markup,
    html = html
)
