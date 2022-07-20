package com.bottlerocketstudios.brarchitecture.domain.models

data class Link(
    val href: String?,
    val name: String? = null
) : DomainModel
