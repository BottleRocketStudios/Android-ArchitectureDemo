package com.bottlerocketstudios.brarchitecture.di

import com.bottlerocketstudios.brarchitecture.buildconfig.BuildConfigProviderImpl
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.ToasterImpl
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.auth.AuthCodeViewModel
import com.bottlerocketstudios.brarchitecture.ui.devoptions.ApplicationInfoManager
import com.bottlerocketstudios.brarchitecture.ui.devoptions.ApplicationInfoManagerImpl
import com.bottlerocketstudios.brarchitecture.ui.devoptions.DevOptionsViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.profile.ProfileViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.snippet.CreateSnippetFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.snippet.SnippetsFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** General app configuration (repositories/viewmodels/etc) */
object AppModule {
    val module = module {
        viewModel { MainActivityViewModel(repo = get(), buildConfigProvider = get()) }
        viewModel { SplashViewModel() }
        viewModel { AuthCodeViewModel() }
        viewModel { HomeViewModel() }
        viewModel { RepositoryBrowserViewModel() }
        viewModel { RepositoryFileFragmentViewModel() }
        viewModel { SnippetsFragmentViewModel(repo = get()) }
        viewModel { CreateSnippetFragmentViewModel(repo = get()) }
        viewModel { ProfileViewModel(repo = get()) }
        viewModel { DevOptionsViewModel(app = get(), forceCrashLogicImpl = get(), applicationInfoManager = get(), environmentRepository = get()) }

        single<ApplicationInfoManager> { ApplicationInfoManagerImpl(app = get(), buildConfigProvider = get()) }
        single<BuildConfigProvider> { BuildConfigProviderImpl() }
        single<Toaster> { ToasterImpl(app = get()) }
    }
}
