package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CredentialModelTest : BaseTest() {
    @Test
    fun credentialModel_shouldHaveFields_whenConstructed() {
        val cm = CredentialModel("id", "password")
        assertThat(cm.id).isEqualTo("id")
        assertThat(cm.password).isEqualTo("password")
    }

    @Test
    fun getValidCredentialModel_shouldReturnNull_whenCredentialsInvalid() {
        val cm = CredentialModel(null, null)
        assertThat(cm.validCredentials).isNull()
    }
}
