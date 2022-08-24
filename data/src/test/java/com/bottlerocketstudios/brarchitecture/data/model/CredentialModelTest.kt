package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.models.CredentialModel
import com.bottlerocketstudios.brarchitecture.domain.models.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.domain.utils.ProtectedProperty
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
    fun valid_shouldReturnTrue_whenIdPasswordAreValid() {
        val cm = CredentialModel(ProtectedProperty("validId"), ProtectedProperty("password0"))
        assertThat(cm.valid).isEqualTo(true)
    }

    @Test
    fun valid_shouldReturnFalse_whenIdPasswordAreValid() {
        val cm = CredentialModel(ProtectedProperty("id"), ProtectedProperty("password"))
        assertThat(cm.valid).isEqualTo(false)
    }

    @Test
    fun getValidCredentialModel_shouldReturnValidCm_whenCredentialsValid() {
        val cm = CredentialModel(ProtectedProperty("validId"), ProtectedProperty("password0"))
        assertThat(cm.validCredentials).isNotNull()
        assertThat(cm.validCredentials).isInstanceOf(ValidCredentialModel::class.java)
    }

    @Test
    fun getValidCredentialModel_shouldReturnNull_whenCredentialsInvalid() {
        val cm = CredentialModel(ProtectedProperty(""), ProtectedProperty(""))
        assertThat(cm.validCredentials).isNull()
    }
}
