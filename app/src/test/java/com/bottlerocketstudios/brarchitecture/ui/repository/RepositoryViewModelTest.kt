package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.KoinTestRule
import com.bottlerocketstudios.brarchitecture.test.TestModule
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.mockito.kotlin.mock
import org.junit.Test

class RepositoryViewModelTest : BaseTest() {

    @get:Rule
    val koinRule = KoinTestRule(TestModule.generateMockedTestModule())

    @Test
    fun repositoryViewModel_shouldHaveMembers_whee() {
        val rvm = RepositoryViewModel(mock {})

        assertThat(rvm.repository).isNotNull()
        assertThat(rvm.getItem(0)).isInstanceOf(ViewModelItem::class.java)
    }
}
