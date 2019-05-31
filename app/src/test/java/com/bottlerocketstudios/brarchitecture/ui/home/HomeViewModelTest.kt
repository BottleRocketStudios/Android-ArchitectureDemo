package com.bottlerocketstudios.brarchitecture.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.bottlerocketstudios.brarchitecture.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.model.Repository
import com.bottlerocketstudios.brarchitecture.domain.model.User
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.ScopedViewModel
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers.Unconfined
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
    val repo: BitbucketRepository = mock {
        on {user}.then {_user}
        on {repos}.then {_repos}
        on {refreshUser()}.then {
            val user = User(username=TEST_USER_NAME)
            _user.value = user
            true
        }
        on {refreshMyRepos()}.then {
            val repos = listOf(Repository(name="testRepo"))
            _repos.value = repos
            true
        }
    }

    @Test
    fun homeViewModel_shouldUpdateAdapter_whenReposRefreshed() {
        ScopedViewModel.context = Unconfined
        val model = HomeViewModel(mock{}, repo)
        assertThat(model.repos.value).hasSize(1)
        assertThat(model.reposGroup.itemCount).isEqualTo(1)
    }
    
    @Test
    fun homeViewModel_shouldHaveNoObservers_whenCleared() {
        ScopedViewModel.context = Unconfined
        val model = HomeViewModel(mock{}, repo)
        assertThat(model.repos.hasObservers()).isTrue()
        model.doClear()
        assertThat(model.repos.hasObservers()).isFalse()
    }

    @Test
    fun homeViewModel_shouldHaveUser_whenInitialized() {
        ScopedViewModel.context = Unconfined
        val model = HomeViewModel(mock{}, repo)
        assertThat(model.user).isNotNull()
        assertThat(model.user.value?.username).isEqualTo(TEST_USER_NAME)
    }
}