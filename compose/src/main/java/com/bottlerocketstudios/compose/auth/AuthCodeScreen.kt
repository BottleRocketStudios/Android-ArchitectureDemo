package com.bottlerocketstudios.compose.auth

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.Colors
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewAll
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.compose.widgets.OutlinedSurfaceButton
import com.bottlerocketstudios.compose.widgets.PrimaryButton
import com.bottlerocketstudios.compose.widgets.SurfaceButton
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

@Composable
fun AuthCodeScreen(state: AuthCodeState, navigator: WebViewNavigator) {
    Crossfade(targetState = state.requestUrl.value.isEmpty()) {
        if (it) {
            state.showToolbar(false)
            AuthCodeContent(state)
        } else {
            state.showToolbar(true)
            RequestAuth(url = state.requestUrl.value, onAuthCode = state.onAuthCode, navigator = navigator)
        }
    }
}


// FIXME: javaScriptEnabled = true can result in removal from Play Store
// https://support.google.com/faqs/answer/7668153?hl=en
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RequestAuth(url: String, onAuthCode: (String) -> Unit, navigator: WebViewNavigator) {
    val state: WebViewState = rememberWebViewState(url = url)

    if (state.content.getCurrentUrl()?.contains("www.bottlerocketstudios.com") == true) {
        onAuthCode(Uri.parse(state.content.getCurrentUrl() ?: "").getQueryParameter("code") ?: "")
    }

    WebView(
        state = state,
        navigator = navigator,
        onCreated = {
            it.settings.javaScriptEnabled = true
        }
    )
}

@Composable
private fun AuthCodeContent(state: AuthCodeState) {
    Column(
        modifier = Modifier
            // .fillMaxHeight()
            .fillMaxSize()
            .background(Colors.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.splash_image_content_description)
        )

        Text(
            modifier = Modifier
                .padding(top = Dimens.grid_2)
                .padding(start = Dimens.grid_2, end = Dimens.grid_2),
            text = stringResource(R.string.auth_code_subtitle),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h2,
            color = Colors.onBackground,
        )

        Text(
            modifier = Modifier
                .padding(top = Dimens.grid_2)
                .padding(start = Dimens.grid_2, end = Dimens.grid_2),
            text = stringResource(R.string.auth_code_description),
            style = MaterialTheme.typography.h3,
            color = Colors.onBackground,
            textAlign = TextAlign.Center,
        )

        Column(
            modifier = Modifier
                .padding(top = Dimens.grid_3)
                .fillMaxWidth(fraction = .4f),
            horizontalAlignment = Alignment.CenterHorizontally,
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
            if (state.devOptionsEnabled) {
                OutlinedSurfaceButton(
                    buttonText = stringResource(id = R.string.dev_options_button),
                    forceCaps = true,
                    onClick = state.onDevOptionsClicked,
                    modifier = Modifier.padding(top = Dimens.grid_4)
                )
            }
        }
    }
}


@PreviewAll
@Composable
fun AuthCodePreview() {
    Preview {
        AuthCodeScreen(
            state = AuthCodeState(
                requestUrl = "".asMutableState(),
                devOptionsEnabled = true,
                onAuthCode = {},
                onLoginClicked = {},
                onSignupClicked = {},
                onDevOptionsClicked = {},
                showToolbar = {}
            ),
            navigator = rememberWebViewNavigator()
        )
    }
}

