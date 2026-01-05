package com.bottlerocketstudios.brarchitecture.ui

import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_HASH
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_PATH
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_REPO
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_REPO_MIME
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutesTest : BaseTest() {
    @Test
    fun repositoryBrowser_dataHasFields_shouldReturnCorrectRoutes() {
        val route = Routes.RepositoryBrowser(TEST_REPO, TEST_HASH, TEST_PATH)
        assertThat(route.repoName).isEqualTo(TEST_REPO)
        assertThat(route.folderHash).isEqualTo(TEST_HASH)
        assertThat(route.folderPath).isEqualTo(TEST_PATH)
    }

    @Test
    fun repositoryBrowser_emptyDataFields_shouldReturnCorrectRoutes() {
        val route = Routes.RepositoryBrowser(TEST_REPO)
        assertThat(route.repoName).isEqualTo(TEST_REPO)
        assertThat(route.folderHash).isNull()
        assertThat(route.folderPath).isNull()
    }

    @Test
    fun repositoryFile_dataHasFields_shouldReturnCorrectRoutes() {
        val route = Routes.RepositoryFile(TEST_HASH, TEST_PATH, TEST_REPO_MIME)
        assertThat(route.hash).isEqualTo(TEST_HASH)
        assertThat(route.path).isEqualTo(TEST_PATH)
        assertThat(route.mimeType).isEqualTo(TEST_REPO_MIME)
    }

    @Test
    fun repositoryFile_noMimeType_shouldReturnCorrectRoutes() {
        val route = Routes.RepositoryFile(TEST_HASH, TEST_PATH)
        assertThat(route.hash).isEqualTo(TEST_HASH)
        assertThat(route.path).isEqualTo(TEST_PATH)
        assertThat(route.mimeType).isEqualTo("")
    }
}
