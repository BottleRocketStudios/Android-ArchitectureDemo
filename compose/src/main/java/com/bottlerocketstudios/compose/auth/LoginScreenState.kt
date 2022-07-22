package com.bottlerocketstudios.compose.auth

import androidx.compose.runtime.State

data class LoginScreenState(
    val email: State<String>,
    val password: State<String>,
    val loginEnabled: State<Boolean>,
    val devOptionsEnabled: Boolean,
    val onLoginClicked: () -> Unit,
    val onSignupClicked: () -> Unit,
    val onForgotClicked: () -> Unit,
    val onDevOptionsClicked: () -> Unit,
    val onEmailChanged: (String) -> Unit,
    val onPasswordChanged: (String) -> Unit
)
