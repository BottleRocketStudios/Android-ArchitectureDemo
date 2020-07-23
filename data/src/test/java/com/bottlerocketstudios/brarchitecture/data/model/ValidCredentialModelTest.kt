package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
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
