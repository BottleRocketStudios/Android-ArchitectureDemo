package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_REPO
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RepositoryBrowserDataTest : BaseTest() {
    @Test
    fun repositoryBrowserDataTest_defaultFields_whenDefaultConstructor() {
        val repoBrowserData = RepositoryBrowserData(TEST_REPO)
        assertThat(repoBrowserData.repoName).isEqualTo(TEST_REPO)
        assertThat(repoBrowserData.folderHash).isEqualTo(null)
        assertThat(repoBrowserData.folderPath).isEqualTo(null)
    }
}
