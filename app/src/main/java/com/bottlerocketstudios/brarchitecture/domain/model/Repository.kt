package com.bottlerocketstudios.brarchitecture.domain.model


data class Repository (
    val scm: String? = "",
    val name: String? = "",
    val is_private: Boolean? = true
)
