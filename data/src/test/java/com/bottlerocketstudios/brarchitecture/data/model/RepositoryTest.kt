package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RepositoryTest : BaseTest() {
    @Test
    fun repository_shouldHaveFields_whenConstructed() {
        val repository = Repository("scm", "name", null, true)
        assertThat(repository.scm).isEqualTo("scm")
        assertThat(repository.name).isEqualTo("name")
        assertThat(repository.isPrivate).isTrue()
    }
}
