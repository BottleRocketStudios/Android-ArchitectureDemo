package com.bottlerocketstudios.compose.home

import com.bottlerocketstudios.brarchitecture.domain.models.Link
import com.bottlerocketstudios.brarchitecture.domain.models.Links
import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.brarchitecture.domain.models.Workspace
import com.bottlerocketstudios.compose.util.StringIdHelper
import java.time.ZonedDateTime

internal val testCard1 = UserRepositoryUiModel(
    GitRepository(
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
    formattedTime = StringIdHelper.Raw("")
)
internal val testCard2 = UserRepositoryUiModel(
    GitRepository(
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
    formattedTime = StringIdHelper.Raw("")
)
