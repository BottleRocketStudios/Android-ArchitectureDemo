package com.bottlerocketstudios.brarchitecture.ui.user

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bottlerocketstudios.brarchitecture.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserFragmentUiTest {

    @Test
    fun launchAFragment() {
        val scenario = launchFragmentInContainer<UserFragment>(themeResId = R.style.AppTheme)
    }
}
