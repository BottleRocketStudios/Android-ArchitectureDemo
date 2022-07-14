package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.ZonedDateTime

class GitRepositoryTest : BaseTest() {
    @Test
    fun repository_defaultFields_whenDefaultConstructor() {
        val repository = GitRepositoryDto()
        assertThat(repository.scm).isEqualTo("")
        assertThat(repository.name).isEqualTo("")
        assertThat(repository.owner).isEqualTo(null)
        assertThat(repository.workspaceDto).isEqualTo(null)
        assertThat(repository.isPrivate).isEqualTo(true)
        assertThat(repository.description).isEqualTo("")
        assertThat(repository.updated).isEqualTo(null)
    }

    @Test
    fun repository_shouldHaveFields_whenConstructed() {
        val user = UserDto()
        val workspace = WorkspaceDto()
        val updated = ZonedDateTime.parse("2022-07-09T17:09:43.365424Z[UTC]")

        val repository = GitRepositoryDto("scm", "name", user, workspace, true, "description", updated)
        assertThat(repository.scm).isEqualTo("scm")
        assertThat(repository.name).isEqualTo("name")
        assertThat(repository.owner).isEqualTo(user)
        assertThat(repository.workspaceDto).isEqualTo(workspace)
        assertThat(repository.isPrivate).isTrue()
        assertThat(repository.description).isEqualTo("description")
        assertThat(repository.updated).isEqualTo(updated)
    }
}
