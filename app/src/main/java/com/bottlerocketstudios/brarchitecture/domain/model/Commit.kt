package com.bottlerocketstudios.brarchitecture.domain.model


data class Commit(
    val parents: List<Commit>?,
    val date: String?,
    val message: String?,
    val type: String?,
    val hash: String?
)