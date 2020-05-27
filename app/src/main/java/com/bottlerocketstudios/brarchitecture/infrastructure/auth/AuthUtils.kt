package com.bottlerocketstudios.brarchitecture.infrastructure.auth

import org.apache.commons.codec.binary.Base64

fun getBasicAuthHeader(username: String, password: String): String {
    val auth = String(Base64.encodeBase64("$username:$password".toByteArray()))
    return "Basic $auth"
}
