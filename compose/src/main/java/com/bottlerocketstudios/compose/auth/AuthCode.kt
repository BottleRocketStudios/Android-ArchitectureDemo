package com.bottlerocketstudios.compose.auth

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.widgets.OutlinedSurfaceButton
import com.bottlerocketstudios.compose.widgets.PrimaryButton
import com.bottlerocketstudios.compose.widgets.SurfaceButton

data class AuthCodeState(
    val requestUrl: State<String>,
    val devOptionsEnabled: Boolean,
    val onAuthCode: (String) -> Unit,
    val onLoginClicked: () -> Unit,
    val onSignupClicked: () -> Unit,
    val onDevOptionsClicked: () -> Unit,
)

@Preview(showSystemUi = true)
@Composable
fun AuthCodePreview() {
    Preview {
        AuthCodeScreen(
            state = AuthCodeState(
                requestUrl = "".asState(),
                devOptionsEnabled = true,
                onAuthCode = {},
                onLoginClicked = {},
                onSignupClicked = {},
                onDevOptionsClicked = {},
            )
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RequestAuth(url: String, onAuthCode: (String) -> Unit) {
    AndroidView(factory = {
        WebView(it).apply {
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    //  This has to match callback URL defined for bitbucket api key.
                    if (request?.url?.toString()?.contains("www.bottlerocketstudios.com") == true) {
                        onAuthCode(request.url.getQueryParameter("code") ?: "")
                        return true
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }
            settings.javaScriptEnabled = true
            loadUrl(url)
        }
    })
}

@Composable
fun AuthCodeScreen(state: AuthCodeState) {
    Crossfade(targetState = state.requestUrl.value.isEmpty()) {
        if (it) AuthCodeContent(state) else RequestAuth(url = state.requestUrl.value, onAuthCode = state.onAuthCode)
    }
}

@Composable
private fun AuthCodeContent(state: AuthCodeState) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
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
        )

        Text(
            modifier = Modifier
                .padding(top = Dimens.grid_2)
                .padding(start = Dimens.grid_2, end = Dimens.grid_2),
            text = stringResource(R.string.auth_code_description),
            style = MaterialTheme.typography.h3,
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

@Composable
fun Preview(content: @Composable () -> Unit) = ArchitectureDemoTheme(content = content)

@SuppressLint("UnrememberedMutableState")
@Composable
fun <T> T.asState() = mutableStateOf(this)
