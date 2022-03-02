package com.bottlerocketstudios.brarchitecture.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.bottlerocketstudios.compose.auth.LoginScreenState

@Composable
fun LoginViewModel.toState() = LoginScreenState(
    email = email.collectAsState(),
    password = password.collectAsState(),
    loginEnabled = loginEnabled.collectAsState(),
    devOptionsEnabled = devOptionsEnabled,
    onLoginClicked = { onLoginClicked() },
    onSignupClicked = { onSignupClicked() },
    onForgotClicked = { onForgotClicked() },
    onDevOptionsClicked = { onDevOptionsClicked() },
    onEmailChanged = { value -> email.value = value },
    onPasswordChanged = { value -> password.value = value }
)
