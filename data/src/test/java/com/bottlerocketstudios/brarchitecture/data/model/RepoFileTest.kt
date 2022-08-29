package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RepoFileTest : BaseTest() {
    @Test
    fun repoFile_defaultFields_whenDefaultConstructor() {
        val repoFile = RepoFileDto(null, null, null, null, null, null)
        assertThat(repoFile.type).isNull()
        assertThat(repoFile.path).isNull()
        assertThat(repoFile.mimetype).isNull()
        assertThat(repoFile.attributes).isNull()
        assertThat(repoFile.size).isNull()
        assertThat(repoFile.commit).isNull()
    }
}
