package com.bottlerocketstudios.brarchitecture.domain.models

data class Links(
    val self: Link?,
    val html: Link?,
    val comments: Link?,
    val watchers: Link?,
    val commits: Link?,
    val avatar: Link?,
) : DomainModel
