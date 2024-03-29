package com.bottlerocketstudios.brarchitecture.test.mocks

import com.bottlerocketstudios.brarchitecture.data.converter.convertToGitRepository
import com.bottlerocketstudios.brarchitecture.data.converter.convertToSnippet
import com.bottlerocketstudios.brarchitecture.data.converter.toPullRequest
import com.bottlerocketstudios.brarchitecture.data.converter.toRepoFile
import com.bottlerocketstudios.brarchitecture.data.converter.toUser
import com.bottlerocketstudios.brarchitecture.data.model.GitRepositoryDto
import com.bottlerocketstudios.brarchitecture.data.model.LinkDto
import com.bottlerocketstudios.brarchitecture.data.model.LinksDto
import com.bottlerocketstudios.brarchitecture.data.model.PullRequestDto
import com.bottlerocketstudios.brarchitecture.data.model.RepoFileDto
import com.bottlerocketstudios.brarchitecture.data.model.SnippetDto
import com.bottlerocketstudios.brarchitecture.data.model.UserDto
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.mockito.kotlin.mock
import java.time.ZonedDateTime

const val TEST_USER_NAME = "test_user_name"
const val TEST_USER_DISPLAY_NAME = "test_user_display_name"
const val TEST_USER_NICKNAME = "test_user_nickname"
const val TEST_USER_LINK = "test_user_link"
const val TEST_REPO = "test_repo"
const val TEST_REPO_ID = "test_repo_id"
const val TEST_REPO_MIME = "test_repo-mime"
const val TEST_HASH = "test_hash"
const val TEST_PATH = "test_path"
const val TEST_TYPE = "commit_directory"
const val SNIPPET_ID = "snippet_id"
const val SNIPPET_TITLE = "snippet_title"
const val SNIPPET_FILENAME = "snippet_filename"
const val SNIPPET_CONTENTS = "snippet_contents"
const val ZONE_DATE_TIME = "2022-07-09T17:09:43.365424Z[UTC]"

object MockBitBucketRepo {
    private val _user = MutableStateFlow<UserDto?>(null)
    private val _pullRequests = MutableStateFlow<List<PullRequestDto>>(emptyList())
    private val _repos = MutableStateFlow<List<GitRepositoryDto>>(emptyList())
    private val _snippets = MutableStateFlow<List<SnippetDto>>(emptyList())

    var authenticated = true
    var isPrivate = false
    var causeFailure = false

    var testRepoFile = RepoFileDto(TEST_TYPE, TEST_PATH, "", listOf(""), 0, null)
    var testGitRepositoryDto = GitRepositoryDto(name = TEST_REPO, updated = ZonedDateTime.parse(ZONE_DATE_TIME))
    var testGitRepositoryDtoList = listOf(testGitRepositoryDto)
    var testUserDto = UserDto(
        username = TEST_USER_NAME,
        displayName = TEST_USER_DISPLAY_NAME,
        nickname = TEST_USER_NICKNAME,
        linksDto = LinksDto(avatar = LinkDto(href = TEST_USER_LINK, name = "user"))
    )
    var snippetDto = SnippetDto(
        id = SNIPPET_ID,
        title = SNIPPET_TITLE,
        isPrivate = isPrivate,
        owner = testUserDto,
        updated = ZonedDateTime.parse(ZONE_DATE_TIME)
    )

    val bitbucketRepository: BitbucketRepository = mock {
        on { user }.then { _user.map { it?.toUser() } }
        on { pullRequests }.then { _pullRequests.map { list -> list.map { it.toPullRequest() } } }
        on { repos }.then { _repos.map { list -> list.map { it.convertToGitRepository() } } }
        on { snippets }.then { _snippets.map { list -> list.map { it.convertToSnippet() } } }

        onBlocking { authenticate("") }.then { authenticated }
        onBlocking { authenticate(null) }.then { authenticated }
        onBlocking { clear() }.then {
            authenticated = false
            _user.value = null
            _repos.value = emptyList()
            _snippets.value = emptyList()
            _pullRequests.value = emptyList()
            Unit
        }
        onBlocking { refreshUser() }.then {
            _user.value = testUserDto
            Status.Success(Unit)
        }
        onBlocking { refreshMyRepos() }.then {
            _repos.value = testGitRepositoryDtoList
            Status.Success(Unit)
        }
        onBlocking { refreshMySnippets() }.then {
            _snippets.value = listOf(snippetDto)
            Status.Success(listOf(snippetDto.convertToSnippet()))
        }
        onBlocking { getSourceFolder("", TEST_REPO, TEST_HASH, TEST_PATH) }.then {
            Status.Success(listOf(testRepoFile.toRepoFile()))
        }
        onBlocking { getSource("", TEST_REPO) }.then {
            Status.Success(listOf(testRepoFile.toRepoFile()))
        }
        onBlocking { getSourceFile("", TEST_REPO_ID, TEST_HASH, TEST_PATH) }.then {
            Status.Success(byteArrayOf(1, 2, 3, 4))
        }
        onBlocking { createSnippet(SNIPPET_TITLE, SNIPPET_FILENAME, SNIPPET_CONTENTS, isPrivate) }.then {
            when (causeFailure) {
                false -> Status.Success(Unit)
                true -> Status.Failure.GeneralFailure("")
            }
        }
    }
}
