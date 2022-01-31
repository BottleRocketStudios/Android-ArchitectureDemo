package com.bottlerocketstudios.brarchitecture.data.network.auth

import com.bottlerocketstudios.brarchitecture.data.model.ProtectedProperty
import org.apache.commons.codec.binary.Base64

internal fun getBasicAuthHeader(username: ProtectedProperty<String>, password: ProtectedProperty<String>): String {
    val auth = String(Base64.encodeBase64("${username.value}:${password.value}".toByteArray()))
    return "Basic $auth"
}
