package com.bottlerocketstudios.brarchitecture

import com.bottlerocketstudios.brarchitecture.infrastructure.auth.AuthRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.BitbucketCredentialsRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.TokenAuthRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.auth.LoginViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.user.UserActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { TokenAuthRepository(get()) }
    single { BitbucketRepository(get()) }
    single { BitbucketCredentialsRepository(get()) }
    viewModel { SplashActivityViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { RepositoryActivityViewModel(get(), get()) }
    viewModel { UserActivityViewModel(get(), get()) }
}
