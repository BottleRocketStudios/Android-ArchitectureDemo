package com.bottlerocketstudios.brarchitecture.domain.model

data class CredentialModel(val id: String?, val password: String?) {
    fun isIdValid(): Boolean {
        return id!=null && id.length > 3
    }

    fun isPasswordValid(): Boolean {
        return password != null && password.length >= 8 && password.contains(Regex("[0-9]"))
    }
    
    val valid: Boolean
        get() = isIdValid() && isPasswordValid()
    
    val validCredentials: ValidCredentialModel?
        get() = if (valid) ValidCredentialModel(id!!, password!!) else null
}

data class ValidCredentialModel(val id: String, val password: String) 
