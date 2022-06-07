package com.bottlerocketstudios.brarchitecture.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.auth.AuthCodeState

@Composable
fun AuthCodeViewModel.toState(showToolbar: (show: Boolean) -> Unit) = AuthCodeState(
    requestUrl = requestUrl.collectAsState(),
    devOptionsEnabled = devOptionsEnabled,
    onLoginClicked = ::onLoginClicked,
    onSignupClicked = ::onSignUpClicked,
    onDevOptionsClicked = ::onDevOptionsClicked,
    onAuthCode = ::onAuthCode,
    showToolbar = showToolbar,
)
