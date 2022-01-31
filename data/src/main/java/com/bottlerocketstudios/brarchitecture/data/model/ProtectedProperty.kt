package com.bottlerocketstudios.brarchitecture.data.model

/**
 * Wrapper around [value] that doesn't print sensitive information via toString in logs/exception stacktraces/etc
 *
 * Inspired by implementation from https://mustafaali.net/2018/01/14/Kotlin-data-classes-and-sensitive-information/
 */
data class ProtectedProperty<T>(val value: T) {
    override fun toString(): String {
        return if (value == null) {
            "ProtectedProperty(value=$value)"
        } else {
            "ProtectedProperty(value=REDACTED)"
        }
    }
}

fun <T> T.toProtectedProperty() = ProtectedProperty(this)
