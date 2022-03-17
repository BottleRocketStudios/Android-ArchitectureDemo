package com.bottlerocketstudios.compose.home

import com.bottlerocketstudios.brarchitecture.domain.models.Link
import com.bottlerocketstudios.brarchitecture.domain.models.Links
import com.bottlerocketstudios.brarchitecture.domain.models.Repository
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.brarchitecture.domain.models.Workspace
import java.time.ZonedDateTime

val testCard1 = UserRepositoryUiModel(
    Repository(
        scm = "scm1",
        name = "name1",
        owner = User(
            username = "username1",
            nickname = "nickname1",
            accountStatus = "accountStatus1",
            displayName = "displayName1",
            createdOn = "createdOn1",
            uuid = "uuid1",
            links = Links(
                avatar = Link(
                    href = "href1"
                )
            ),
            avatarUrl = "avatarUrl1"
        ),
        workspace = Workspace(
            slug = "slug1",
            name = "name1",
            uuid = "uuid1"
        ),
        isPrivate = false,
        description = "description1",
        updated = ZonedDateTime.now()
    ),
    updatedTimeString = "2022 02 01"
)
val testCard2 = UserRepositoryUiModel(
    Repository(
        scm = "scm2",
        name = "name2",
        owner = User(
            username = "username2",
            nickname = "nickname2",
            accountStatus = "accountStatus2",
            displayName = "displayName2",
            createdOn = "createdOn2",
            uuid = "uuid2",
            links = Links(
                avatar = Link(
                    href = "href2"
                )
            ),
            avatarUrl = "avatarUrl2"
        ),
        workspace = Workspace(
            slug = "slug2",
            name = "name2",
            uuid = "uuid2"
        ),
        isPrivate = false,
        description = "description2",
        updated = ZonedDateTime.now()
    ),
    updatedTimeString = "2022 02 02"
)
