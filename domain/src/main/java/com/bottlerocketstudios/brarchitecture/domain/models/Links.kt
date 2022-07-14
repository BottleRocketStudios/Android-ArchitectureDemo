package com.bottlerocketstudios.brarchitecture.domain.models

data class Links(
    val self: Link? = null,
    val html: Link? = null,
    val comments: Link? = null,
    val watchers: Link? = null,
    val commits: Link? = null,
    val avatar: Link? = null,
) : DomainModel
