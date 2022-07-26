package com.bottlerocketstudios.compose.snippets.snippetDetails

import androidx.compose.runtime.Composable
import com.bottlerocketstudios.brarchitecture.domain.models.Link
import com.bottlerocketstudios.brarchitecture.domain.models.Links
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetComment
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetCommentContent
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.compose.util.asMutableState
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

val mockCreationMsg = ZonedDateTime.now().minusDays(30).convertToTimeAgoMessage()
val mockUpdatedMsg = ZonedDateTime.now().minusHours(18).convertToTimeAgoMessage()

@Composable
fun returnMockSnippetDetails() =
    SnippetDetailsScreenState(
        snippetDetails = SnippetDetailsUiModel(
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
                links = Links(
                    comments = Link("https://fake.snippet.com/comments", name = "")
                )
            ),
            SnippetDetailsFile(
                fileName = "Test File Number 2",
                links = Links(
                    comments = Link("https://fake.snippet.com/comments", name = "")
                )
            ),
            SnippetDetailsFile(
                fileName = "Test File Number 3",
                links = Links(
                    comments = Link("https://fake.snippet.com/comments", name = "")
                )
            ),
            SnippetDetailsFile(
                fileName = "Test File Number 4",
                links = Links(
                    comments = Link("https://fake.snippet.com/comments", name = "")
                )
            ),
        ).asMutableState(),

        isWatchingSnippet = false.asMutableState(),
        onSnippetWatchClick = { },
        onSnippetEditClick = { },
        onSnippetDeleteClick = { },
        comments = listOf(snippetComment).asMutableState(),
        newSnippetComment = "".asMutableState(),
        onCommentChanged =  {},
        onSaveCommentClick = {},
        onCancelNewCommentClick = {},
        onDeleteCommentClick = {},
        onEditCommentClick = {},
        newReplyComment = "".asMutableState(),
        onReplyChanged = {}
    )

fun ZonedDateTime.convertToTimeAgoMessage(): String {
    val elapsedMinutes = TimeUnit.SECONDS.toMinutes(
        ZonedDateTime.now().toEpochSecond() - this.toEpochSecond()
    )

    val elapsedHours = TimeUnit.MINUTES.toHours(elapsedMinutes)
    val elapsedDays = TimeUnit.MINUTES.toDays(elapsedMinutes)

    return when {
        elapsedMinutes in 5..59 -> "$elapsedMinutes Minutes Ago"
        elapsedHours in 1..23 -> "$elapsedHours Hours Ago"
        elapsedDays in 1..6 -> "$elapsedDays Days Ago"
        elapsedDays in 7..30 -> "${(elapsedDays / 7).toInt()} Weeks Ago"
        elapsedDays in 31..365 -> "${(elapsedDays / 30.4).roundToInt()} Months Ago"
        elapsedDays > 364 -> "${(elapsedDays / 365).toInt()} Years Ago"
        else -> "Just Now"
    }
}

val currentUser = User(
    username = null,
    nickname = null,
    displayName = "Luke Skywalker",
    avatarUrl = "https://i.pinimg.com/736x/69/ed/be/69edbedeccf27136c2ea6b18af6ec49d.jpg",
    accountStatus = null,
    createdOn = null,
    uuid = null,
    links = null
)
val snippetComment = SnippetComment(
    id = 7,
    type = "Snippet Comment",
    user = currentUser,
    parentId = null,
    childrenComments = mutableListOf(
        SnippetComment(
            id = 7,
            type = "Snippet Comment",
            user = currentUser,
            parentId = null,
            childrenComments = mutableListOf(
                SnippetComment(
                    id = 7,
                    type = "Snippet Comment",
                    user = currentUser,
                    parentId = null,
                    childrenComments = mutableListOf<SnippetComment>(),
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
    ),
    content = SnippetCommentContent(raw = "This is a fake comment on a preivew of a comment card. This represents the comment a user would make.", html = null, markup = null, type = null),
    created = mockCreationMsg,
    updated = mockCreationMsg,
    deleted = false
)


