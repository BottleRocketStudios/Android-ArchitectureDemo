package com.bottlerocketstudios.compose.snippets

import androidx.compose.runtime.Composable
import com.bottlerocketstudios.brarchitecture.domain.models.Link
import com.bottlerocketstudios.brarchitecture.domain.models.Links
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.compose.util.asMutableState
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

val mockCreationMsg = ZonedDateTime.now().minusDays(30).convertToModifiedMessage()
val mockUpdatedMsg = ZonedDateTime.now().minusHours(18).convertToModifiedMessage()

@Composable
fun returnMockSnippetDetails() =
     SnippetDetailsScreenState(
    snippetTitle = "Test Snippet Title".asMutableState(),
    createdMessage = mockCreationMsg.asMutableState(),
    updatedMessage = mockUpdatedMsg.asMutableState(),
    isPrivate = false.asMutableState(),
    files = listOf(
        SnippetDetailsFile(
            fileName = "Test File Number 1",
            links = Links(
                comments = Link("https://fake.snippet.com/comments")
            )
        ),
        SnippetDetailsFile(
            fileName = "Test File Number 2",
            links = Links(
                comments = Link("https://fake.snippet.com/comments")
            )
        ),
        SnippetDetailsFile(
            fileName = "Test File Number 3",
            links = Links(
                comments = Link("https://fake.snippet.com/comments")
            )
        ),
        SnippetDetailsFile(
            fileName = "Test File Number 4",
            links = Links(
                comments = Link("https://fake.snippet.com/comments")
            )
        ),
    ).asMutableState(),
    owner = User(
        username = "Anakin",
        nickname = "Annie",
        accountStatus = "",
        displayName = "Darth Vader",
        createdOn = "",
        uuid = "",
        links = Links(null),
        avatarUrl = ""
    ).asMutableState(),
    creator = User(
        username = "Obi-Wan",
        nickname = "Ben",
        accountStatus = "",
        displayName = "Obi-Wan Kenobi",
        createdOn = "",
        uuid = "",
        links = Links(null),
        avatarUrl = ""
    ).asMutableState()
)

fun ZonedDateTime.convertToModifiedMessage(): String {
    val timeElapsed =
        ZonedDateTime.now().toEpochSecond() - this.toEpochSecond()

    val minutes = TimeUnit.SECONDS.toMinutes(timeElapsed)

    return when {
        minutes in 2..59 -> "$minutes Minutes Ago"
        minutes > 60 && (minutes/60) < 24 -> "${TimeUnit.MINUTES.toHours(minutes).toInt()} Hours Ago"
        (minutes/60) > 24 && ((minutes/60)/24) < 30.4 -> "${TimeUnit.MINUTES.toDays(minutes).toInt()} Days Ago"
        TimeUnit.MINUTES.toDays(minutes) > 30.4 -> "${(TimeUnit.MINUTES.toDays(minutes)/30.4).toInt()} Months Ago"
        else -> "Unknown"
    }
}
