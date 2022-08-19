package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.domain.utils.ProtectedProperty
import com.squareup.moshi.JsonClass

data class CredentialModel(val id: ProtectedProperty<String>, val password: ProtectedProperty<String>) {
    private val isIdValid: Boolean = id.value.length > MINIMUM_ID_LENGTH
    private val isPasswordValid: Boolean = password.value.length >= MINIMUM_PASSWORD_LENGTH && password.value.contains(Regex("[0-9]"))
    val valid: Boolean = isIdValid && isPasswordValid
    val validCredentials: ValidCredentialModel? = if (valid) ValidCredentialModel(id, password) else null
}

private const val MINIMUM_ID_LENGTH = 3
private const val MINIMUM_PASSWORD_LENGTH = 8

@JsonClass(generateAdapter = true)
data class ValidCredentialModel(val id: ProtectedProperty<String>, val password: ProtectedProperty<String>)
