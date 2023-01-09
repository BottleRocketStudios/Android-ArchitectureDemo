package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.AuthorDto
import com.bottlerocketstudios.brarchitecture.data.model.CommitDto
import com.bottlerocketstudios.brarchitecture.data.model.CommitRepositoryDto
import com.bottlerocketstudios.brarchitecture.data.model.UserDto
import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CommitDtoConverterTest : BaseTest() {
    @Test
    fun commit_shouldCreateCommit_whenConvertToCommit() {
        val commitDto = CommitDto(
            null,
            null,
            "Message",
            "Type",
            "123rty",
            AuthorDto(
                UserDto(
                    "Name",
                    "Nickname",
                    "Open",
                    "Now",
                    "12345",
                    "12345",
                    null
                ),
                "Author", null,
                "None",
                "12345",
                "Id",
                "Nickname"
            ),
            CommitRepositoryDto(
                "Name",
                "Full Name",
                "Type"
            ),
            null
        )
        val commit = commitDto.toCommit()
        assertThat(commit.hash).isEqualTo(commitDto.hash)
        assertThat(commit.author).isEqualTo(commitDto.author?.toAuthor())
        assertThat(commit.date).isEqualTo(commitDto.date)
        assertThat(commit.message).isEqualTo(commitDto.message)
    }
}
