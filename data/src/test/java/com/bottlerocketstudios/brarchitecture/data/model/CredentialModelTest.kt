package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CredentialModelTest : BaseTest() {
    @Test
    fun credentialModel_shouldHaveFields_whenConstructed() {
        val cm = CredentialModel(ProtectedProperty("id"), ProtectedProperty("password"))
        assertThat(cm.id.value).isEqualTo("id")
        assertThat(cm.password.value).isEqualTo("password")
    }

    @Test
    fun getValidCredentialModel_shouldReturnNull_whenCredentialsInvalid() {
        val cm = CredentialModel(ProtectedProperty(""), ProtectedProperty(""))
        assertThat(cm.validCredentials).isNull()
    }
}
