package com.bottlerocketstudios.brarchitecture.data.model

import com.squareup.moshi.JsonClass

data class CredentialModel(val id: String?, val password: String?) {
    fun isIdValid(): Boolean {
        return id != null && id.length > MINIMUM_ID_LENGTH
    }

    fun isPasswordValid(): Boolean {
        return password != null && password.length >= MINIMUM_PASSWORD_LENGTH && password.contains(Regex("[0-9]"))
    }

    val valid: Boolean
        get() = isIdValid() && isPasswordValid()

    val validCredentials: ValidCredentialModel?
        get() = if (valid) ValidCredentialModel(id!!, password!!) else null
}

private const val MINIMUM_ID_LENGTH = 3
private const val MINIMUM_PASSWORD_LENGTH = 8

@JsonClass(generateAdapter = true)
data class ValidCredentialModel(val id: String, val password: String)
