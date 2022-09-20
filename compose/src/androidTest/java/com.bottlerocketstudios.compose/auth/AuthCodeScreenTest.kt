package com.bottlerocketstudios.compose.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.util.asMutableState
import com.google.accompanist.web.WebViewNavigator
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.Rule
import org.junit.Test

class AuthCodeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val webViewNavigator = WebViewNavigator(testScope)

    @Test
    fun authCodeScreenImage_doesHaveContentDescription() {
        // State
        val state = AuthCodeState(
            requestUrl = "".asMutableState(),
            devOptionsEnabled = false,
            onAuthCode = {},
            onLoginClicked = {},
            onSignupClicked = {},
            onDevOptionsClicked = {},
            showToolbar = {}
        )

        composeTestRule.setContent {
            ArchitectureDemoTheme {
                AuthCodeScreen(state, webViewNavigator)
            }
        }

        val authCodeScreenImageContentDescription = composeTestRule.activity.resources.getString(R.string.splash_image_content_description)
        // retrieve compose image instance using content desc as identifier
        val authCodeScreenImage = composeTestRule.onNodeWithContentDescription(authCodeScreenImageContentDescription)
        authCodeScreenImage.assertIsDisplayed()
    }

    @Test
    fun devOptionsButton_doesNotExist() {
        // State
        val state = AuthCodeState(
            requestUrl = "".asMutableState(),
            devOptionsEnabled = false,
            onAuthCode = {},
            onLoginClicked = {},
            onSignupClicked = {},
            onDevOptionsClicked = {},
            showToolbar = {}
        )

        composeTestRule.setContent {
            ArchitectureDemoTheme {
                AuthCodeScreen(state, webViewNavigator)
            }
        }

        val devOptionsButtonText = composeTestRule.activity.resources.getString(R.string.dev_options_button).uppercase()
        // retrieve compose button instance using button text as identifier
        val devOptionsButton = composeTestRule.onNodeWithText(devOptionsButtonText)
        devOptionsButton.assertDoesNotExist()
    }

    @Test
    fun devOptionsButton_doesExist_andIsDisplayed() {
        // State
        val state = AuthCodeState(
            requestUrl = "".asMutableState(),
            devOptionsEnabled = true,
            onAuthCode = {},
            onLoginClicked = {},
            onSignupClicked = {},
            onDevOptionsClicked = {},
            showToolbar = {}
        )

        composeTestRule.setContent {
            ArchitectureDemoTheme {
                AuthCodeScreen(state, webViewNavigator)
            }
        }

        val devOptionsButtonText = composeTestRule.activity.resources.getString(R.string.dev_options_button).uppercase()
        // retrieve compose button instance using button text as identifier
        val loginButton = composeTestRule.onNodeWithText(devOptionsButtonText)
        loginButton.assertIsDisplayed()
        loginButton.assertExists()
    }

    @Test
    fun loginButton_isClicked() {
        // State
        var onLoginWasClicked = false
        val state = AuthCodeState(
            requestUrl = "".asMutableState(),
            devOptionsEnabled = true,
            onAuthCode = {},
            onLoginClicked = { onLoginWasClicked = true },
            onSignupClicked = {},
            onDevOptionsClicked = {},
            showToolbar = {}
        )

        composeTestRule.setContent {
            ArchitectureDemoTheme {
                AuthCodeScreen(state, webViewNavigator)
            }
        }

        val loginButtonText = composeTestRule.activity.resources.getString(R.string.login_button).uppercase()
        // retrieve compose button instance using button text as identifier
        val loginButton = composeTestRule.onNodeWithText(loginButtonText)
        loginButton.performClick()
        loginButton.assertTextEquals(loginButtonText)
        assert(onLoginWasClicked)
    }
}
