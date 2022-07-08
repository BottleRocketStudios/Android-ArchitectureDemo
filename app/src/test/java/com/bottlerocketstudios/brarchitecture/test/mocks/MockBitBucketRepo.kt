package com.bottlerocketstudios.brarchitecture.test.mocks

import com.bottlerocketstudios.brarchitecture.data.model.GitRepositoryDto
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.UserDto
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import kotlinx.coroutines.flow.MutableStateFlow
import org.mockito.kotlin.mock

const val TEST_USER_NAME = "test_user"
const val TEST_SLUG = "test_slug"
const val TEST_REPO = "test_repo"
const val TEST_HASH = "test_hash"
const val TEST_PATH = "test_path"
const val TEST_TYPE = "commit_directory"

object MockBitBucketRepo {
    private val _user = MutableStateFlow<UserDto?>(null)
    private val _repos = MutableStateFlow<List<GitRepositoryDto>>(emptyList())

    var testRepoFile = RepoFile(TEST_TYPE, TEST_PATH, "", listOf(""), 0, null)
    var testGitRepositoryDtoList = listOf(GitRepositoryDto(name = TEST_REPO))

    var causeFailure = false

    val bitbucketRepository: BitbucketRepository = mock {
        on { user }.then { _user }
        on { repos }.then { _repos }
        onBlocking { refreshUser() }.then {
            _user.value = UserDto(username = TEST_USER_NAME)
            Status.Success(Unit)
        }
        onBlocking { refreshMyRepos() }.then {
            _repos.value = testGitRepositoryDtoList
            Status.Success(Unit)
        }
        onBlocking { getSourceFolder(TEST_SLUG, TEST_REPO, TEST_HASH, TEST_PATH) }.then {
            Status.Success(listOf(testRepoFile))
        }
        onBlocking { getSource("", TEST_REPO) }.then {
            Status.Success(listOf(testRepoFile))
        }
    }

}



