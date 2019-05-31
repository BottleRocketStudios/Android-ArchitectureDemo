package com.bottlerocketstudios.brarchitecture.ui

import com.bottlerocketstudios.brarchitecture.BaseTest
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class ScopedViewModelTest : BaseTest() {
    @Test
    fun scopedViewModel_shouldHaveCoroutineContext_whee() {
        val svm = ScopedViewModel(mock{})
        assertThat(svm.coroutineContext).isNotNull()
        assertThat(ScopedViewModel.context).isNotNull()
    }
}