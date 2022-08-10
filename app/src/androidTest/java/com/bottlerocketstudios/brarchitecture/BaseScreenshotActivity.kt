package com.bottlerocketstudios.brarchitecture

import android.util.Log
import androidx.test.ext.junit.rules.activityScenarioRule
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivity
import com.karumi.shot.ScreenshotTest
import org.junit.Rule
import org.junit.Test

class BaseScreenshotActivity : ScreenshotTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<ComposeActivity>()

    @Test
    fun activityTest() {
        val activity = launchActivity()
        compareScreenshot(activity)
    }

    // Hack needed until we fully support Activity Scenarios
    private fun launchActivity(): ComposeActivity {
        var activity: ComposeActivity? = null
        activityScenarioRule.scenario.onActivity {
            activity = it
        }
        while (activity == null) {
            Log.d("MainActivityTest", "Waiting for activity to be initialized")
        }
        return activity!!
    }
}
