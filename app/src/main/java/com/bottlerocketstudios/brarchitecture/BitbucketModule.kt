package com.bottlerocketstudios.brarchitecture

import com.bottlerocketstudios.brarchitecture.infrastructure.auth.AuthRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.TokenAuthRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.network.DefaultOkHttpBuilderProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.network.OkHttpBuilderProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.auth.LoginViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFolderFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.user.UserFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<OkHttpBuilderProvider> { DefaultOkHttpBuilderProvider(get()) }
    single<AuthRepository> { TokenAuthRepository(get()) }
    single { BitbucketRepository(get(), get()) }
    single { BitbucketCredentialsRepository(get()) }
    viewModel { MainActivityViewModel(get(), get()) }
    viewModel { SplashFragmentViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { RepositoryFragmentViewModel(get(), get()) }
    viewModel { RepositoryFileFragmentViewModel(get(), get()) }
    viewModel { RepositoryFolderFragmentViewModel(get(), get()) }
    viewModel { UserFragmentViewModel(get(), get()) }
}
