package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class WorkspaceDtoTest: BaseTest() {
    @Test
    fun workspaceDto_defaultFields_whenDefaultConstructor() {
        val workspaceDto = WorkspaceDto()
        assertThat(workspaceDto.slug).isEqualTo("")
        assertThat(workspaceDto.name).isEqualTo("")
        assertThat(workspaceDto.uuid).isEqualTo("")
    }

    @Test
    fun workspaceDto_shouldHaveFields_whenConstructed() {
        val workspaceDto = WorkspaceDto("test_slug", "test_name", "test_uuid" )
        assertThat(workspaceDto.slug).isEqualTo("test_slug")
        assertThat(workspaceDto.name).isEqualTo("test_name")
        assertThat(workspaceDto.uuid).isEqualTo("test_uuid")
    }
}
