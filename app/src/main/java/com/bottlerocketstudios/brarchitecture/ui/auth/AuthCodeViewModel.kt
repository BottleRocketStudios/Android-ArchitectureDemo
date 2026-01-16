package com.bottlerocketstudios.brarchitecture.ui.auth

import android.content.Intent
import androidx.core.net.toUri
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.BuildConfig.BITBUCKET_KEY
import com.bottlerocketstudios.brarchitecture.data.BuildConfig.COGNITO_CLIENT_ID
import com.bottlerocketstudios.brarchitecture.data.BuildConfig.COGNITO_DOMAIN
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.domain.repositories.CognitoRepository
import com.bottlerocketstudios.brarchitecture.navigation.ExternalNavigationEvent
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.component.inject

class AuthCodeViewModel : BaseViewModel() {
    // DI
    private val repo: BitbucketRepository by inject()
    private val cognitoRepo: CognitoRepository by inject()
    private val buildConfigProvider: BuildConfigProvider by inject()

    // UI
    val requestUrl = MutableStateFlow("")
    val devOptionsEnabled = buildConfigProvider.isDebugOrInternalBuild

    // Events
    val devOptionsEvent: SharedFlow<Unit> = MutableSharedFlow()
    val homeEvent: SharedFlow<Unit> = MutableSharedFlow()

    // /////////////////////////////////////////////////////////////////////////
    // Callbacks
    // /////////////////////////////////////////////////////////////////////////
    private var isCognitoLogin = false

    fun onLoginClicked() {
        isCognitoLogin = false
        requestUrl.value = "https://bitbucket.org/site/oauth2/authorize?client_id=$BITBUCKET_KEY&response_type=code"
    }

    fun onCognitoLoginClicked() {
        isCognitoLogin = true
        val domain = COGNITO_DOMAIN
        val redirectUri = "https://www.bottlerocketstudios.com"
        val baseUrl = if (domain.startsWith("http")) domain else "https://$domain"
        val url = "$baseUrl.auth.us-east-1.amazoncognito.com/login?response_type=code&scope=email+openid+profile&client_id=$COGNITO_CLIENT_ID&redirect_uri=$redirectUri"
        requestUrl.value = url
    }

    fun onDevOptionsClicked() {
        launchIO { devOptionsEvent.emit(Unit) }
    }

    fun onSignUpClicked() =
        externalNavigationEvent.postValue(ExternalNavigationEvent(Intent(Intent.ACTION_VIEW, "https://id.atlassian.com/signup?application=bitbucket".toUri())))

    fun onAuthCode(authCode: String) {
        requestUrl.value = ""

        launchIO {
            val authenticated = if (isCognitoLogin) {
                cognitoRepo.authenticate(authCode)
            } else {
                repo.authenticate(authCode)
            }

            if (authenticated) {
                homeEvent.emit(Unit)
            } else {
                handleError(R.string.login_error)
            }
        }
    }
}
