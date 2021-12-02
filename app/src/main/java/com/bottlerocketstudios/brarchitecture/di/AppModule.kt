package com.bottlerocketstudios.brarchitecture.di

import com.bottlerocketstudios.brarchitecture.buildconfig.BuildConfigProviderImpl
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.ToasterImpl
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.auth.LoginViewModel
import com.bottlerocketstudios.brarchitecture.ui.devoptions.DevOptionsViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFolderFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.snippet.CreateSnippetFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.snippet.SnippetsFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.user.UserFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** General app configuration (repositories/viewmodels/etc) */
object AppModule {
    val module = module {
        viewModel { MainActivityViewModel(repo = get(), buildConfigProvider = get()) }
        viewModel { SplashFragmentViewModel(repo = get(), dispatcherProvider = get()) }
        viewModel { LoginViewModel(repo = get(), buildConfigProvider = get(), toaster = get(), dispatcherProvider = get()) }
        viewModel { HomeViewModel(repo = get(), dispatcherProvider = get()) }
        viewModel { RepositoryFragmentViewModel(repo = get(), toaster = get(), dispatcherProvider = get()) }
        viewModel { RepositoryFileFragmentViewModel(repo = get(), toaster = get(), dispatcherProvider = get()) }
        viewModel { RepositoryFolderFragmentViewModel(repo = get(), toaster = get(), dispatcherProvider = get()) }
        viewModel { SnippetsFragmentViewModel(repo = get(), dispatcherProvider = get()) }
        viewModel { CreateSnippetFragmentViewModel(repo = get(), dispatcherProvider = get()) }
        viewModel { UserFragmentViewModel(repo = get()) }
        viewModel { DevOptionsViewModel(app = get(), forceCrashLogicImpl = get(), environmentRepository = get(), dispatcherProvider = get(), buildConfigProvider = get()) }

        single<BuildConfigProvider> { BuildConfigProviderImpl() }
        single<Toaster> { ToasterImpl(app = get()) }
    }
}
