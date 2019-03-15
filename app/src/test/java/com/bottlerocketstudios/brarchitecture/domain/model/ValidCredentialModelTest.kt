package com.bottlerocketstudios.brarchitecture.domain.model

import com.bottlerocketstudios.brarchitecture.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidCredentialModelTest : BaseTest() {
    @Test
    fun validCredentialModel_shouldHaveFields_whenConstructed() {
        val vcm = ValidCredentialModel("id", "password")
        assertThat(vcm.id).isEqualTo("id")
        assertThat(vcm.password).isEqualTo("password")
    }
}