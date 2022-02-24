package com.bottlerocketstudios.compose.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

data class LoginScreenState(
    val email: State<String>,
    val password: State<String>,
    val loginEnabled: State<Boolean>,
    val devOptionsEnabled: Boolean,
    val onLoginClicked: () -> Unit,
)

@Composable
fun LoginScreen(state: LoginScreenState) {

}
