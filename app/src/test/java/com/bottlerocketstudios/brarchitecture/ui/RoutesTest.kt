package com.bottlerocketstudios.brarchitecture.ui

import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_HASH
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_PATH
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_REPO
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_REPO_MIME
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserData
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileData
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutesTest : BaseTest() {
    @Test
    fun repositoryBrowser_dataHasFields_shouldReturnFields() {
        assertThat(Routes.RepositoryBrowser(RepositoryBrowserData(TEST_REPO, TEST_HASH, TEST_PATH)))
            .isEqualTo("repository?repoName=$TEST_REPO&folderHash=$TEST_HASH&folderPath=$TEST_PATH")
    }

    @Test
    fun repositoryBrowser_emptyDataFields_shouldReturnWithoutFields() {
        assertThat(Routes.RepositoryBrowser(RepositoryBrowserData(TEST_REPO)))
            .isEqualTo("repository?repoName=$TEST_REPO")
    }

    @Test
    fun repositoryFile_dataHasFields_shouldReturnFields() {
        assertThat(Routes.RepositoryFile(RepositoryFileData(TEST_HASH, TEST_PATH, TEST_REPO_MIME)))
            .isEqualTo("file?hash=$TEST_HASH&path=$TEST_PATH&mimeType=$TEST_REPO_MIME")
    }

    @Test
    fun repositoryFile_noMimeType_shouldReturnWithoutMime() {
        assertThat(Routes.RepositoryFile(RepositoryFileData(TEST_HASH, TEST_PATH, "")))
            .isEqualTo("file?hash=$TEST_HASH&path=$TEST_PATH")
    }
}
