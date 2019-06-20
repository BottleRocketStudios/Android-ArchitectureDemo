package com.bottlerocketstudios.brarchitecture.ui.repository

// None of this is going to work
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.auth.LoginFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryFragmentUiTest {
    
    @Test
    fun launchAFragment() {
        val scenario = launchFragmentInContainer<LoginFragment>(themeResId = R.style.AppTheme)
    }
}
