package com.bottlerocketstudios.brarchitecture.di

import com.bottlerocketstudios.brarchitecture.buildconfig.BuildConfigProviderImpl
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.auth.LoginViewModel
import com.bottlerocketstudios.brarchitecture.ui.devoptions.DevOptionsViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFolderFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.user.UserFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** General app configuration (repositories/viewmodels/etc) */
object AppModule {
    val appModule = module {
        viewModel { MainActivityViewModel(get()) }
        viewModel { SplashFragmentViewModel(get(), get(), get()) }
        viewModel { LoginViewModel(get(), get(), get(), get()) }
        viewModel { HomeViewModel(get(), get(), get(), get()) }
        viewModel { RepositoryFragmentViewModel(get(), get(), get()) }
        viewModel { RepositoryFileFragmentViewModel(get(), get(), get()) }
        viewModel { RepositoryFolderFragmentViewModel(get(), get(), get()) }
        viewModel { UserFragmentViewModel(get(), get()) }
        viewModel { DevOptionsViewModel(get(), get(), get(), get(), get()) }

        single<BuildConfigProvider> { BuildConfigProviderImpl() }
    }
}
