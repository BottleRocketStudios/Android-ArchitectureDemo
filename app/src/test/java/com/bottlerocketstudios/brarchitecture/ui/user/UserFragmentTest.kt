package com.bottlerocketstudios.brarchitecture.ui.user

import com.bottlerocketstudios.brarchitecture.infrastructure.utils.BaseRobolectricTest
import org.junit.Before
import org.junit.Test
import org.robolectric.Robolectric
import kotlin.test.assertNotNull

class UserFragmentTest : BaseRobolectricTest() {
    @Before
    fun setup() {
        activity = Robolectric.buildFragment(UserFragment::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testExists() {
        assertNotNull(activity, "UserFragment not created successfully")
    }
}
