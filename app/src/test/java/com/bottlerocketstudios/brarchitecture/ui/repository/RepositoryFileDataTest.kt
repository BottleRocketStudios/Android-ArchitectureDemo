package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_HASH
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_PATH
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_REPO_MIME
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RepositoryFileDataTest: BaseTest() {
    @Test
    fun repoFileData_hasFields_whenConstructed() {
        val repoFileData = RepositoryFileData(TEST_HASH, TEST_PATH, TEST_REPO_MIME)
        assertThat(repoFileData.hash).isEqualTo(TEST_HASH)
        assertThat(repoFileData.path).isEqualTo(TEST_PATH)
        assertThat(repoFileData.mimeType).isEqualTo(TEST_REPO_MIME)
    }
}
