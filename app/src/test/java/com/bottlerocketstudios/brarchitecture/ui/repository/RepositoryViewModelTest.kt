package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.BaseTest
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class RepositoryViewModelTest : BaseTest() {
    @Test
    fun repositoryViewModel_shouldHaveMembers_whee() {
        val rvm = RepositoryViewModel(mock {})
        assertThat(rvm.repository).isNotNull()
        assertThat(rvm.getItem(0)).isInstanceOf(ViewModelItem::class.java)
    }
}
