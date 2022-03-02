package com.bottlerocketstudios.compose.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme

data class AuthCodeState(
    val requestUrl: State<String>,
    val inputCode: State<String>,
    val codeUpdate: (String) -> Unit,
    //  May not need this.
    val newCodeClicked: () -> Unit,
    val loginClicked: ()  -> Unit,
)

@Preview(showSystemUi = true)
@Composable
fun AuthCodePreview() {
    Preview {
        AuthCodeContent(state = AuthCodeState(
            requestUrl = "https://url.com".asState(),
            inputCode = "".asState(),
            codeUpdate = {},
            newCodeClicked = {},
            loginClicked = {},
        ))
    }
}

@Composable
fun AuthCodeContent(state: AuthCodeState) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
    //    TODO - Uncomment after merge.
    // AppIcon()

    //    TODO - bottom buttons first
        Column(Modifier.fillMaxWidth(fraction = .33f)) {
            // PrimaryButton(
            //     buttonText = stringResource(id = R.string.login_button),
            //     forceCaps = true,
            //     onClick = state.onLoginClicked,
            //     modifier = Modifier
            //         .fillMaxWidth()
            //         .padding(top = Dimens.grid_2_5)
            // )
            // SurfaceButton(
            //     buttonText = stringResource(id = R.string.signup_button),
            //     forceCaps = true,
            //     onClick = state.onSignupClicked,
            //     modifier = Modifier.fillMaxWidth()
            // )
            // OutlinedSurfaceButton(
            //     buttonText = stringResource(id = R.string.dev_options_button),
            //     forceCaps = true,
            //     onClick = state.onDevOptionsClicked,
            //     modifier = Modifier.padding(top = Dimens.grid_4)
            // )
        }
    }
}

@Composable
fun Preview(content: @Composable () -> Unit) = ArchitectureDemoTheme(content = content)

@SuppressLint("UnrememberedMutableState")
@Composable
fun <T> T.asState() = mutableStateOf(this)
