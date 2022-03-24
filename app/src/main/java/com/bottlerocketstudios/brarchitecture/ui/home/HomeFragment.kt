package com.bottlerocketstudios.brarchitecture.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserData
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import com.bottlerocketstudios.compose.home.HomeScreen
import com.bottlerocketstudios.compose.home.UserRepositoryUiModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Clock

class HomeFragment : BaseFragment<HomeViewModel>() {
    override val fragmentViewModel: HomeViewModel by viewModel()
    private val activityViewModel: MainActivityViewModel by sharedViewModel()
    private val clock by inject<Clock>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        ComposeScreen {
            val state = fragmentViewModel.toState()
            setUpdatedOnString(state.repositories.value, clock)
            HomeScreen(state = state, ::selectItem)
        }

    private fun selectItem(userRepositoryUiModel: UserRepositoryUiModel) {
        Toast.makeText(activity, userRepositoryUiModel.repo.name as CharSequence, Toast.LENGTH_SHORT).show()
        activityViewModel.selectedRepo.value = userRepositoryUiModel.repo
        val action = HomeFragmentDirections.actionHomeToRepositoryBrowserFragment(
            RepositoryBrowserData(
                repoName = userRepositoryUiModel.repo.name ?: ""
            )
        )
        findNavController(this@HomeFragment).navigate(action)
    }

    private fun setUpdatedOnString(userRepositoryUiModelList: List<UserRepositoryUiModel>, clock: Clock) {
        userRepositoryUiModelList.forEach { userRepoUiModel ->
            context?.let {
                userRepoUiModel.updatedTimeString = userRepoUiModel.repo.updated.formattedUpdateTime(clock = clock).getString(it)
            }
        }
    }
}
