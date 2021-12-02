package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bottlerocketstudios.brarchitecture.data.model.ApiResult
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.model.User
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.KoinTestRule
import com.bottlerocketstudios.brarchitecture.test.TestDispatcherProvider
import com.bottlerocketstudios.brarchitecture.test.TestModule
import com.google.common.truth.Truth.assertThat
import org.mockito.kotlin.mock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest : BaseTest() {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val koinRule = KoinTestRule(TestModule.generateMockedTestModule())

    val _user = MutableStateFlow<User?>(null)
    val _repos = MutableStateFlow<List<Repository>>(emptyList())
    private val TEST_USER_NAME = "testuser"
    private val dispatcherProvider = TestDispatcherProvider()
    val repo: BitbucketRepository = mock {
        on { user }.then { _user }
        on { repos }.then { _repos }
        onBlocking { refreshUser() }.then {
            val user = User(username = TEST_USER_NAME)
            _user.value = user
            ApiResult.Success(Unit)
        }
        onBlocking { refreshMyRepos() }.then {
            val repos = listOf(Repository(name = "testRepo"))
            _repos.value = repos
            ApiResult.Success(Unit)
        }
    }

    @Test
    fun homeViewModel_shouldUpdateAdapter_whenReposRefreshed() = runBlocking {
        val model = HomeViewModel(repo, dispatcherProvider)

        assertThat(model.repos.value).hasSize(1)
        assertThat(model.reposGroup.itemCount).isEqualTo(2)
    }

    @Test
    fun homeViewModel_shouldHaveUser_whenInitialized() = runBlocking {
        val model = HomeViewModel(repo, dispatcherProvider)

        assertThat(model.user).isNotNull()
        assertThat(model.user.value?.username).isEqualTo(TEST_USER_NAME)
    }
}
