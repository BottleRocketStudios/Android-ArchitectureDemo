package com.bottlerocketstudios.brarchitecture

import com.bottlerocketstudios.brarchitecture.ui.MainActivityViewModel
import com.bottlerocketstudios.brarchitecture.ui.auth.LoginViewModel
import com.bottlerocketstudios.brarchitecture.ui.home.HomeViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFolderFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.splash.SplashFragmentViewModel
import com.bottlerocketstudios.brarchitecture.ui.user.UserFragmentViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** General app configuration (repositories/viewmodels/etc) */
object App {
    val appModule = module {
        viewModel { MainActivityViewModel(get()) }
        viewModel { SplashFragmentViewModel(get(), get(), get()) }
        viewModel { LoginViewModel(get(), get(), get()) }
        viewModel { HomeViewModel(get(), get(), get()) }
        viewModel { RepositoryFragmentViewModel(get(), get(), get()) }
        viewModel { RepositoryFileFragmentViewModel(get(), get(), get()) }
        viewModel { RepositoryFolderFragmentViewModel(get(), get(), get()) }
        viewModel { UserFragmentViewModel(get(), get()) }
    }
}

/** Allows multiple types of network related implementations to co-exist in the koin graph */
private enum class KoinNamedNetwork {
    Unauthenticated, Authenticated
}

