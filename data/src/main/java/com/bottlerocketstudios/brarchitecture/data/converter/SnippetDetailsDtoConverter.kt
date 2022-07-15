package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.LinksDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDetailsDto
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.compose.snippets.SnippetDetailsUiModel
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

fun SnippetDetailsDto.convertToUiModel() =
    SnippetDetailsUiModel(
        id = id,
        title = title,
        createdMessage = created?.convertToModifiedMessage(),
        updatedMessage = updated?.convertToModifiedMessage(),
        isPrivate = isPrivate,
        files = files?.convertToUiModel(),
        owner = owner?.convertToUser(),
        creator = creator?.convertToUser()
    )


fun Map<String, LinksDto?>?.convertToUiModel(): List<SnippetDetailsFile?>? =
    this?.map { SnippetDetailsFile(it.key, it.value?.convertToLinks()) }

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



