package com.bottlerocketstudios.compose.snippets.snippetDetails

import androidx.compose.runtime.Composable
import com.bottlerocketstudios.brarchitecture.domain.models.Links
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetCommentContent
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetails
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.brarchitecture.domain.utils.convertToTimeAgoMessage
import com.bottlerocketstudios.compose.util.asMutableState
import java.time.ZonedDateTime

val mockCreationMsg = ZonedDateTime.now().minusDays(30).convertToTimeAgoMessage()
val mockUpdatedMsg = ZonedDateTime.now().minusHours(18).convertToTimeAgoMessage()

@Composable
internal fun returnMockSnippetDetails() =
    SnippetDetailsScreenState(
        snippetDetails = SnippetDetails(
            id = "fake id",
            title = "Test Snippet Title",
            createdMessage = mockCreationMsg,
            updatedMessage = mockUpdatedMsg,
            isPrivate = false,
            owner = User(
                username = "Anakin",
                nickname = "Annie",
                accountStatus = "",
                displayName = "Darth Vader",
                createdOn = "",
                uuid = "",
                links = Links(null),
                avatarUrl = "https://whatsondisneyplus.com/wp-content/uploads/2022/05/vader.png"
            ),
            creator = User(
                username = "Obi-Wan",
                nickname = "Ben",
                accountStatus = "",
                displayName = "Obi-Wan Kenobi",
                createdOn = "",
                uuid = "",
                links = Links(null),
                avatarUrl = "https://whatsondisneyplus.com/wp-content/uploads/2022/05/kenobi-avatar.png"
            ),
            httpsCloneLink = "https://bitbucket.org/snippets/jmichealmurray/zEqaLk/test-snippet-public.git",
            sshCloneLink = "git@bitbucket.org:snippets/jmichealmurray/zEqaLk/test-snippet-public.git"
        ).asMutableState(),
        currentUser = currentUser.asMutableState(),
        files = listOf(
            SnippetDetailsFile(
                fileName = "Test File Number 1",
                rawFile = ByteArray(1)
            ),
            SnippetDetailsFile(
                fileName = "Test File Number 2",
                rawFile = ByteArray(1)
            ),
            SnippetDetailsFile(
                fileName = "Test File Number 3",
                rawFile = ByteArray(1)
            ),
            SnippetDetailsFile(
                fileName = "Test File Number 4",
                rawFile = ByteArray(1)
            ),
        ).asMutableState(),

        isWatchingSnippet = false.asMutableState(),
        onSnippetWatchClick = { },
        onSnippetEditClick = { },
        onSnippetDeleteClick = { },
        comments = listOf(snippetComment).asMutableState(),
        newSnippetComment = "".asMutableState(),
        onCommentChanged = {},
        onSaveCommentClick = {},
        onCancelNewCommentClick = {},
        onDeleteCommentClick = {},
        onEditCommentClick = {},
        newReplyComment = "".asMutableState(),
        onReplyChanged = {}
    )

internal val currentUser = User(
    displayName = "Luke Skywalker",
    avatarUrl = "https://i.pinimg.com/736x/69/ed/be/69edbedeccf27136c2ea6b18af6ec49d.jpg",
)
internal val snippetComment = SnippetComment(
    id = 7,
    type = "Snippet Comment",
    user = currentUser,
    childrenComments = mutableListOf(
        SnippetComment(
            id = 7,
            type = "Snippet Comment",
            user = currentUser,
            childrenComments = mutableListOf(
                SnippetComment(
                    id = 7,
                    type = "Snippet Comment",
                    user = currentUser,
                    childrenComments = mutableListOf<SnippetComment>(),
                    content = SnippetCommentContent(
                        raw = "This is a fake comment on a preivew of a comment card. This represents the comment a user would make.",
                        html = null,
                        markup = null,
                        type = null
                    ),
                    created = mockCreationMsg,
                    updated = mockCreationMsg,
                    deleted = false
                )
            ),
            content = SnippetCommentContent(raw = "This is a fake comment on a preivew of a comment card. This represents the comment a user would make.", html = null, markup = null, type = null),
            created = mockCreationMsg,
            updated = mockCreationMsg,
            deleted = false
        )
    ),
    content = SnippetCommentContent(raw = "This is a fake comment on a preivew of a comment card. This represents the comment a user would make.", html = null, markup = null, type = null),
    created = mockCreationMsg,
    updated = mockCreationMsg,
    deleted = false
)
