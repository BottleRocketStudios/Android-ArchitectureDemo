package com.bottlerocketstudios.compose.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.widgets.OutlinedSurfaceButton
import com.bottlerocketstudios.compose.widgets.PrimaryButton
import com.bottlerocketstudios.compose.widgets.SurfaceButton

@Composable
fun LoginScreen(state: LoginScreenState) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppIcon()
            TextFields(state = state)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                SurfaceButton(
                    buttonText = stringResource(id = R.string.login_forgot),
                    onClick = state.onForgotClicked,
                    modifier = Modifier.padding(end = Dimens.grid_5)
                )
            }
            Row(
                modifier = Modifier.padding(bottom = Dimens.grid_6)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PrimaryButton(
                        buttonText = stringResource(id = R.string.login_button),
                        forceCaps = true,
                        onClick = state.onLoginClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.grid_2_5)
                    )
                    SurfaceButton(
                        buttonText = stringResource(id = R.string.signup_button),
                        forceCaps = true,
                        onClick = state.onSignupClicked,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedSurfaceButton(
                        buttonText = stringResource(id = R.string.dev_options_button),
                        forceCaps = true,
                        onClick = state.onDevOptionsClicked,
                        modifier = Modifier.padding(top = Dimens.grid_4)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun AppIcon() {
    Image(
        modifier = Modifier.padding(top = Dimens.grid_6),
        painter = painterResource(id = R.drawable.ic_logo),
        contentDescription = stringResource(id = R.string.splash_image_content_description)
    )
}

@Composable
fun TextFields(state: LoginScreenState) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            primary = MaterialTheme.colors.onBackground
        )
    ) {
        val localFocusManager = LocalFocusManager.current
        LoginScreenUsernameTextField(
            modifier = Modifier
                .padding(
                    start = Dimens.grid_6,
                    end = Dimens.grid_6,
                    top = Dimens.grid_2
                )
                .fillMaxWidth(),
            valueText = state.email.value,
            onValueChange = state.onEmailChanged,
            onNextClicked = {
                localFocusManager.moveFocus(FocusDirection.Down)
            }
        )
        LoginScreenPasswordTextField(
            modifier = Modifier
                .padding(
                    start = Dimens.grid_6,
                    end = Dimens.grid_6,
                    top = Dimens.grid_2
                )
                .fillMaxWidth(),
            valueText = state.password.value,
            onValueChange = state.onPasswordChanged,
            onDoneClicked = {
                localFocusManager.clearFocus()
                state.onLoginClicked()
            }
        )
    }
}

@Composable
fun LoginScreenUsernameTextField(
    modifier: Modifier,
    valueText: String,
    onValueChange: (String) -> Unit,
    onNextClicked: () -> Unit
) {
    val focused = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier.onFocusChanged {
            focused.value = it.isFocused
        },
        value = valueText,
        onValueChange = onValueChange,
        label = {
            Text(stringResource(id = R.string.login_id_hint))
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = if (focused.value) {
                MaterialTheme.colors.primary
            } else {
                LocalContentColor.current
            }
        ),
        leadingIcon = {
            Icon(
                modifier = Modifier.padding(end = Dimens.grid_0_5),
                painter = painterResource(id = R.drawable.ic_nav_profile),
                contentDescription = stringResource(id = R.string.login_id_accessibility),
                tint = if (focused.value) MaterialTheme.colors.primary else LocalContentColor.current
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onNextClicked()
            }
        ),
        trailingIcon = {
            if (valueText.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .padding(start = Dimens.grid_0_5)
                        .clickable {
                            onValueChange("")
                        },
                    painter = painterResource(id = R.drawable.ic_clear),
                    contentDescription = stringResource(id = R.string.login_clear_accessibility),
                )
            }
        }
    )
}

@Suppress("LongMethod")
@Composable
fun LoginScreenPasswordTextField(
    modifier: Modifier,
    valueText: String,
    onValueChange: (String) -> Unit,
    onDoneClicked: () -> Unit
) {
    val showPassword = remember { mutableStateOf(false) }
    val focused = remember { mutableStateOf(false) }
    val visualTransformation = if (showPassword.value) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    OutlinedTextField(
        modifier = modifier.onFocusChanged {
            focused.value = it.isFocused
        },
        value = valueText,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = if (focused.value) {
                MaterialTheme.colors.primary
            } else {
                LocalContentColor.current
            }
        ),
        label = {
            Text(stringResource(id = R.string.login_password_hint))
        },
        leadingIcon = {
            Icon(
                modifier = Modifier.padding(end = Dimens.grid_0_5),
                painter = painterResource(id = R.drawable.ic_lock),
                contentDescription = stringResource(id = R.string.login_password_accessibility),
                tint = if (focused.value) MaterialTheme.colors.primary else LocalContentColor.current
            )
        },
        visualTransformation = visualTransformation,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDoneClicked()
            }
        ),
        trailingIcon = {
            if (valueText.isNotEmpty()) {
                val showPassResources = if (showPassword.value) {
                    Pair(R.drawable.ic_show, R.string.login_show_password_accessibility)
                } else {
                    Pair(R.drawable.ic_hide, R.string.login_hide_password_accessibility)
                }

                Icon(
                    modifier = Modifier
                        .padding(start = Dimens.grid_0_5)
                        .clickable {
                            showPassword.value = !showPassword.value
                        },
                    painter = painterResource(id = showPassResources.first),
                    contentDescription = stringResource(id = showPassResources.second),
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewOuterScreenContent() {
    Preview {
        LoginScreen(
            state = LoginScreenState(
                email = remember { mutableStateOf("test@test.com") },
                password = remember { mutableStateOf("abc123") },
                loginEnabled = remember { mutableStateOf(true) },
                devOptionsEnabled = true,
                onLoginClicked = {},
                onSignupClicked = {},
                onForgotClicked = {},
                onDevOptionsClicked = {},
                onPasswordChanged = {},
                onEmailChanged = {}
            )
        )
    }
}
