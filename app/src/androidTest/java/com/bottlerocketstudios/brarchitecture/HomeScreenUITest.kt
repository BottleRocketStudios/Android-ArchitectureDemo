package com.bottlerocketstudios.brarchitecture

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.bottlerocketstudios.brarchitecture.domain.models.GitRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Link
import com.bottlerocketstudios.brarchitecture.domain.models.Links
import com.bottlerocketstudios.brarchitecture.domain.models.User
import com.bottlerocketstudios.brarchitecture.domain.models.Workspace
import com.bottlerocketstudios.compose.home.HomeScreen
import com.bottlerocketstudios.compose.home.HomeScreenState
import com.bottlerocketstudios.compose.home.UserRepositoryUiModel
import com.bottlerocketstudios.compose.util.StringIdHelper
import com.bottlerocketstudios.compose.util.asMutableState
import org.junit.Rule
import org.junit.Test
import java.time.ZonedDateTime

class HomeScreenUITest : BaseUIScreenshotTest() {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun homeScreenWithListItemsScreenshot() {
        renderComponent(
            HomeScreenState(
                repositories = listOf(testCard1, testCard2).asMutableState(),
                itemSelected = {}
            )
        )

        compareScreenshot(composeRule.onRoot())
    }

    @Test
    fun homeScreenNoResultItemsScreenshot() {
        renderComponent(
            HomeScreenState(
                repositories = emptyList<UserRepositoryUiModel>().asMutableState(),
                itemSelected = {}
            )
        )

        compareScreenshot(composeRule.onRoot())
    }

    fun renderComponent(state: HomeScreenState) { composeRule.setContent { HomeScreen(state) } }

    // todo move somewhere that both UI test and Compose Preview
    //  can share it instead of setting it twice.
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
                        href = "href1", name = ""
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
        formattedLastUpdatedTime = StringIdHelper.Raw("")
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
                        href = "href2", name = ""
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
        formattedLastUpdatedTime = StringIdHelper.Raw("")
    )
}
