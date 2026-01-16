package com.bottlerocketstudios.brarchitecture.domain.models

data class CognitoUser(
    val sub: String,
    val email: String? = null,
    val username: String? = null,
    val name: String? = null
)
