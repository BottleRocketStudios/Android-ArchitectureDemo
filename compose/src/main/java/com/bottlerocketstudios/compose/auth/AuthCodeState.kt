package com.bottlerocketstudios.compose.auth

import androidx.compose.runtime.State

data class AuthCodeState(
    val requestUrl: State<String>,
    val devOptionsEnabled: Boolean,
    val onAuthCode: (String) -> Unit,
    val onLoginClicked: () -> Unit,
    val onSignupClicked: () -> Unit,
    val onDevOptionsClicked: () -> Unit,
    val showToolbar: (show: Boolean) -> Unit,
)
