package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CommitTest : BaseTest() {
    @Test
    fun commit_defaultFields_whenDefaultConstructor() {
        val commit = CommitDto(null, null, null, null, null, null, null, null)
        assertThat(commit.parents).isNull()
        assertThat(commit.date).isNull()
        assertThat(commit.message).isNull()
        assertThat(commit.type).isNull()
        assertThat(commit.hash).isNull()
        assertThat(commit.author).isNull()
        assertThat(commit.commitRepository).isNull()
        assertThat(commit.links).isNull()
    }
}
