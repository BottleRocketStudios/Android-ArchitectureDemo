package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.TypeTextAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.auth.LoginFragment
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryFragmentUiTest {

    @Test
    fun launchAFragment() {
        val scenario = launchFragmentInContainer<LoginFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.login)).check(matches(not(isEnabled())))
        onView(withId(R.id.id)).perform(TypeTextAction("test@example.com"))
        onView(withId(R.id.password)).perform(TypeTextAction("password1"))
        onView(withId(R.id.login)).check(matches(isEnabled()))
    }
}
