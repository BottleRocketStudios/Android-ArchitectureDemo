package com.bottlerocketstudios.brarchitecture.di

import com.bottlerocketstudios.brarchitecture.buildconfig.BuildConfigProviderImpl
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.ToasterImplementation
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
    val appModule = module {
        viewModel { MainActivityViewModel(app = get(), repo = get(), buildConfigProvider = get()) }
        viewModel { SplashFragmentViewModel(app = get(), repo = get(), dispatcherProvider = get()) }
        viewModel { LoginViewModel(app = get(), repo = get(), buildConfigProvider = get(), toaster = get(), dispatcherProvider = get()) }
        viewModel { HomeViewModel(app = get(), repo = get(), dispatcherProvider = get()) }
        viewModel { RepositoryFragmentViewModel(app = get(), repo = get(), toaster = get(), dispatcherProvider = get()) }
        viewModel { RepositoryFileFragmentViewModel(app = get(), repo = get(), toaster = get(), dispatcherProvider = get()) }
        viewModel { RepositoryFolderFragmentViewModel(app = get(), repo = get(), toaster = get(), dispatcherProvider = get()) }
        viewModel { SnippetsFragmentViewModel(app = get(), repo = get(), dispatcherProvider = get()) }
        viewModel { CreateSnippetFragmentViewModel(app = get(), repo = get(), dispatcherProvider = get()) }
        viewModel { UserFragmentViewModel(app = get(), repo = get()) }
        viewModel { DevOptionsViewModel(app = get(), forceCrashLogicImpl = get(), environmentRepository = get(), buildConfigProvider = get(), dispatcherProvider = get()) }

        single<BuildConfigProvider> { BuildConfigProviderImpl() }
        single<Toaster> { ToasterImplementation(app = get()) }
    }
}
