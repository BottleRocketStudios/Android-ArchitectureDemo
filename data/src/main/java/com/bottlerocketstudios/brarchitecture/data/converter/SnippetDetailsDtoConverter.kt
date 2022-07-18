package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.LinksDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDetailsDto
import com.bottlerocketstudios.brarchitecture.domain.models.SnippetDetailsFile
import com.bottlerocketstudios.compose.snippets.SnippetDetailsUiModel
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

fun SnippetDetailsDto.convertToUiModel() =
    SnippetDetailsUiModel(
        id = id,
        title = title,
        createdMessage = created?.convertToTimeAgoMessage(),
        updatedMessage = updated?.convertToTimeAgoMessage(),
        isPrivate = isPrivate,
        files = files?.convertToUiModel(),
        owner = owner?.convertToUser(),
        creator = creator?.convertToUser(),
        links = links?.convertToLinks()
    )

fun Map<String, LinksDto?>?.convertToUiModel(): List<SnippetDetailsFile?>? =
    this?.map { SnippetDetailsFile(it.key, it.value?.convertToLinks()) }

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
        elapsedDays in 7..30 -> "${(elapsedDays/7).toInt()} Weeks Ago"
        elapsedDays in 31..365 -> "${(elapsedDays/30.4).roundToInt()} Months Ago"
        elapsedDays > 364 -> "${(elapsedDays/365).toInt()} Years Ago"
        else -> "Just Now"
    }
}



