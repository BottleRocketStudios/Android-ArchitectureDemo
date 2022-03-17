package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bottlerocketstudios.brarchitecture.data.model.RepositoryDto
import com.bottlerocketstudios.brarchitecture.data.model.UserDto
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.models.Status
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.KoinTestRule
import com.bottlerocketstudios.brarchitecture.test.TestDispatcherProvider
import com.bottlerocketstudios.brarchitecture.test.TestModule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.mock

class HomeViewModelTest : BaseTest() {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val koinRule = KoinTestRule(TestModule.generateMockedTestModule())

    val _user = MutableStateFlow<UserDto?>(null)
    val _repos = MutableStateFlow<List<RepositoryDto>>(emptyList())
    private val TEST_USER_NAME = "testuser"
    private val dispatcherProvider = TestDispatcherProvider()
    val repo: BitbucketRepository = mock {
        on { user }.then { _user }
        on { repos }.then { _repos }
        onBlocking { refreshUser() }.then {
            val user = UserDto(username = TEST_USER_NAME)
            _user.value = user
            Status.Success(Unit)
        }
        onBlocking { refreshMyRepos() }.then {
            val repos = listOf(RepositoryDto(name = "testRepo"))
            _repos.value = repos
            Status.Success(Unit)
        }
    }

    @Test
    fun homeViewModel_shouldUpdateAdapter_whenReposRefreshed() = runBlocking {
        val model = HomeViewModel(repo)

        assertThat(model.repos.value).hasSize(1)
    }

    @Test
    fun homeViewModel_shouldHaveUser_whenInitialized() = runBlocking {
        val model = HomeViewModel(repo)

        assertThat(model.user).isNotNull()
        assertThat(model.user.value?.username).isEqualTo(TEST_USER_NAME)
    }
}
