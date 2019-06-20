package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.infrastructure.utils.BaseRobolectricTest
import org.junit.Before
import org.junit.Test
import org.robolectric.Robolectric
import kotlin.test.assertNotNull


class RepositoryFragmentTest : BaseRobolectricTest() {
    @Before
    fun setup() {
        activity = Robolectric.buildFragment(RepositoryFragment::class.java)
                .create()
                .resume()
                .get()
    }
    
    @Test
    fun testExists() {
        assertNotNull(activity, "RepositoryFragment not created successfully")
    }
}
