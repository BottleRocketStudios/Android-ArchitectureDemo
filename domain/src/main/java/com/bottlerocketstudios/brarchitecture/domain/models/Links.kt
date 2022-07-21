package com.bottlerocketstudios.brarchitecture.domain.models

data class Links(
    val self: Link? = null,
    val html: Link? = null,
    val comments: Link? = null,
    val watchers: Link? = null,
    val commits: Link? = null,
    val diff: Link? = null,
    val clone: List<Link?>? = null,
    val patch: Link? = null,
    val avatar: Link? = null,
    val followers: Link? = null,
    val following: Link? = null,
    val repositories: Link? = null
) : DomainModel
