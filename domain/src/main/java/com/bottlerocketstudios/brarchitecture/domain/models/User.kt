package com.bottlerocketstudios.brarchitecture.domain.models

data class User(
    val username: String?,
    val nickname: String?,
    val accountStatus: String?,
    val displayName: String?,
    val createdOn: String?,
    val uuid: String?,
    val links: Links?,
    val avatarUrl: String?
)
