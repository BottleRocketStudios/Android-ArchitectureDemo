package com.bottlerocketstudios.compose.splash

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import org.junit.Rule
import org.junit.Test

class SplashTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun splashImage_containsContentDescription_andIsDisplayed() {
        composeTestRule.setContent {
            ArchitectureDemoTheme {
                SplashScreen()
            }
        }

        val splashImageContentDescription = composeTestRule.activity.resources.getString(R.string.splash_image_content_description)
        // retrieve compose image instance using content desc as identifier
        val splashImage = composeTestRule.onNodeWithContentDescription(splashImageContentDescription)
        splashImage.assertIsDisplayed()
        splashImage.assertExists()
    }
}
