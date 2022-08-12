package com.bottlerocketstudios.brarchitecture.domain.models

data class User(
    val username: String? = null,
    val nickname: String? = null,
    val accountStatus: String? = null,
    val displayName: String? = null,
    val createdOn: String? = null,
    val uuid: String? = null,
    val links: Links? = null,
    val avatarUrl: String? = null,
) : DomainModel
