package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.model.User
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseTest() {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val _user = MutableLiveData<User>()
    val _repos = MutableLiveData<List<Repository>>()
    private val TEST_USER_NAME = "testuser"
    private val dispatcherProvider = TestDispatcherProvider()
    val repo: BitbucketRepository = mock {
        on { user }.then { _user }
        on { repos }.then { _repos }
        on { refreshUser() }.then {
            val user = User(username = TEST_USER_NAME)
            _user.value = user
            true
        }
        on { refreshMyRepos() }.then {
            val repos = listOf(Repository(name = "testRepo"))
            _repos.value = repos
            true
        }
    }

    @Test
    fun homeViewModel_shouldUpdateAdapter_whenReposRefreshed() {
        val model = HomeViewModel(mock {}, repo, mock {}, dispatcherProvider)
        assertThat(model.repos.value).hasSize(1)
        assertThat(model.reposGroup.itemCount).isEqualTo(2)
    }

    @Test
    fun homeViewModel_shouldHaveNoObservers_whenCleared() {
        val model = HomeViewModel(mock {}, repo, mock {}, dispatcherProvider)
        assertThat(model.repos.hasObservers()).isTrue()
        model.doClear()
        assertThat(model.repos.hasObservers()).isFalse()
    }

    @Test
    fun homeViewModel_shouldHaveUser_whenInitialized() {
        val model = HomeViewModel(mock {}, repo, mock {}, dispatcherProvider)
        assertThat(model.user).isNotNull()
        assertThat(model.user.value?.username).isEqualTo(TEST_USER_NAME)
    }
}
