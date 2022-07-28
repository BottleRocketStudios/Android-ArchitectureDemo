package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.ZonedDateTime

class SnippetDtoTest : BaseTest() {
    @Test
    fun commit_defaultFields_whenDefaultConstructor() {
        val snippet = SnippetDto()
        assertThat(snippet.id).isNull()
        assertThat(snippet.title).isNull()
        assertThat(snippet.isPrivate).isNull()
        assertThat(snippet.owner).isNull()
        assertThat(snippet.updated).isNull()
    }

    @Test
    fun commit_shouldHaveFields_whenConstructed() {
        val workspace = WorkspaceDto()
        val user = UserDto()
        val updated = ZonedDateTime.parse("2022-07-09T17:09:43.365424Z[UTC]")
        val snippet = SnippetDto("test_id", workspace, "test_title", user, true, updated)
        assertThat(snippet.id).isEqualTo("test_id")
        assertThat(snippet.workspace).isEqualTo(workspace)
        assertThat(snippet.title).isEqualTo("test_title")
        assertThat(snippet.owner).isEqualTo(user)
        assertThat(snippet.isPrivate).isTrue()
        assertThat(snippet.updated).isEqualTo(updated)
    }
}
