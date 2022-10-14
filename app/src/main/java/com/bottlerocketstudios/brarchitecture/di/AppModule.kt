package com.bottlerocketstudios.brarchitecture.di

import com.bottlerocketstudios.brarchitecture.buildconfig.BuildConfigProviderImpl
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.ToasterImpl
import com.bottlerocketstudios.brarchitecture.ui.ComposeActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.auth.AuthCodeViewModel
import com.bottlerocketstudios.brarchitecture.ui.devoptions.ApplicationInfoManager
import com.bottlerocketstudios.brarchitecture.ui.devoptions.ApplicationInfoManagerImpl
import com.bottlerocketstudios.brarchitecture.ui.devoptions.DevOptionsViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.profile.ProfileViewModel
import com.bottlerocketstudios.brarchitecture.ui.pullrequests.PullRequestViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBranchesViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryCommitViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileViewModel
import com.bottlerocketstudios.brarchitecture.ui.snippet.CreateSnippetViewModel
import com.bottlerocketstudios.brarchitecture.ui.snippet.SnippetDetailsViewModel
import com.bottlerocketstudios.brarchitecture.ui.snippet.SnippetsViewModel
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** General app configuration (repositories/viewmodels/etc) */
object AppModule {
    val module = module {
        viewModel { ComposeActivityViewModel() }
        viewModel { SplashViewModel() }
        viewModel { AuthCodeViewModel() }
        viewModel { HomeViewModel() }
        viewModel { RepositoryBrowserViewModel() }
        viewModel { RepositoryFileViewModel() }
        viewModel { RepositoryCommitViewModel() }
        viewModel { RepositoryBranchesViewModel() }
        viewModel { SnippetsViewModel() }
        viewModel { CreateSnippetViewModel() }
        viewModel { ProfileViewModel() }
        viewModel { DevOptionsViewModel() }
        viewModel { SnippetDetailsViewModel() }
        viewModel { PullRequestViewModel() }

        single<ApplicationInfoManager> { ApplicationInfoManagerImpl(app = get(), buildConfigProvider = get()) }
        single<BuildConfigProvider> { BuildConfigProviderImpl() }
        single<Toaster> { ToasterImpl(app = get()) }
    }
}
