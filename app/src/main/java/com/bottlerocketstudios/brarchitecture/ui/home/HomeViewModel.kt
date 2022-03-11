package com.bottlerocketstudios.brarchitecture.ui.home

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.brarchitecture.ui.HeaderViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
import com.bottlerocketstudios.brarchitecture.ui.util.StringIdHelper
import com.xwray.groupie.Section

class HomeViewModel(repo: BitbucketRepository) : BaseViewModel() {
    val user = repo.user
    val repos = repo.repos
    val reposGroup = Section()

    init {
        launchIO {
            repos.collect { repoList ->
                val map = repoList.map { RepositoryViewModel(it) }
                runOnMain {
                    reposGroup.setHeader(HeaderViewModel(StringIdHelper.Id(R.string.home_repositories)))
                    reposGroup.update(map)
                }
            }
        }

        launchIO {
            repo.refreshUser()
            repo.refreshMyRepos()
        }
    }
}
