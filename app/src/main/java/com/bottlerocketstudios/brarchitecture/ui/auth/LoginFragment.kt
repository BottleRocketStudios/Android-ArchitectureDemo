package com.bottlerocketstudios.brarchitecture.ui.auth

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.buildconfig.BuildConfigProviderImpl
import com.bottlerocketstudios.brarchitecture.data.model.ApiResult
import com.bottlerocketstudios.brarchitecture.data.model.RepoFile
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.data.model.Snippet
import com.bottlerocketstudios.brarchitecture.data.model.User
import com.bottlerocketstudios.brarchitecture.data.model.ValidCredentialModel
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.compose.ArchitectureDemoTheme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<LoginViewModel>() {
    override val fragmentViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose the Composition when viewLifecycleOwner is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )

            setContent {
                OuterScreenContent(viewModel = fragmentViewModel)
            }
        }
    }
}

@Composable
private fun OuterScreenContent(viewModel: LoginViewModel) {
    ArchitectureDemoTheme {
        Surface {
            ScreenContent(viewModel)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ScreenContent(viewModel: LoginViewModel) {
    // var username: String by remember { mutableStateOf("") }
    val email = viewModel.email.observeAsState(initial = "")
    val password = viewModel.password.observeAsState(initial = "")
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.app_name),
            alignment = Alignment.Center,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(15.dp))
        Column(
            modifier = Modifier.padding(horizontal = 44.dp)
        ) {
            val focusManager = LocalFocusManager.current
            val keyboardController = LocalSoftwareKeyboardController.current
            OutlinedTextField(
                value = email.value,
                onValueChange = { viewModel.email.value = it },
                label = {
                    Text(text = stringResource(id = R.string.login_id_hint))
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_nav_grey_profile),
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
            )
            Spacer(modifier = Modifier.height(15.dp))
            // TODO: Hook up password visibility toggle
            var passwordVisibility: Boolean by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = password.value,
                onValueChange = { viewModel.password.value = it },
                label = { Text(text = stringResource(id = R.string.login_password_hint)) },
                leadingIcon = { Icon(painterResource(id = R.drawable.ic_outline_lock_24), contentDescription = null) },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.onLoginClicked()
                        keyboardController?.hide()
                    }
                ),
            )
            Spacer(modifier = Modifier.height(15.dp))
            TextButton(
                onClick = { viewModel.onForgotClicked() },
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(
                    text = stringResource(id = R.string.login_forgot),
                    fontSize = 10.sp,
                )
            }
        }
        Spacer(modifier = Modifier.height(35.dp))
        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 25.dp)
                .width(IntrinsicSize.Max)
        ) {
            Button(
                onClick = { viewModel.onLoginClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(106.dp),
            ) {
                Text(stringResource(id = R.string.login_button).toUpperCase(Locale.current))
            }
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(
                onClick = { viewModel.onSignupClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(106.dp),
            ) {
                Text(stringResource(id = R.string.signup_button).toUpperCase(Locale.current))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(
            onClick = { viewModel.onDevOptionsClicked() },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(stringResource(id = R.string.dev_options_button).toUpperCase(Locale.current))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewOuterScreenContent() {
    OuterScreenContent(
        // TODO: Find a better way to do this (might be using Koin field injection in the ViewModel rather than constructor injection)
        viewModel = LoginViewModel(
            app = Application(),
            repo = object : BitbucketRepository {
                override val user: StateFlow<User?>
                    get() = TODO("Not yet implemented")
                override val repos: StateFlow<List<Repository>>
                    get() = TODO("Not yet implemented")
                override val snippets: StateFlow<List<Snippet>>
                    get() = TODO("Not yet implemented")

                override suspend fun authenticate(creds: ValidCredentialModel?): Boolean {
                    TODO("Not yet implemented")
                }

                override suspend fun refreshUser(): ApiResult<Unit> {
                    TODO("Not yet implemented")
                }

                override suspend fun refreshMyRepos(): ApiResult<Unit> {
                    TODO("Not yet implemented")
                }

                override suspend fun refreshMySnippets(): ApiResult<Unit> {
                    TODO("Not yet implemented")
                }

                override suspend fun getRepositories(workspaceSlug: String): ApiResult<List<Repository>> {
                    TODO("Not yet implemented")
                }

                override suspend fun getRepository(workspaceSlug: String, repo: String): ApiResult<Repository> {
                    TODO("Not yet implemented")
                }

                override suspend fun getSource(workspaceSlug: String, repo: String): ApiResult<List<RepoFile>> {
                    TODO("Not yet implemented")
                }

                override suspend fun getSourceFolder(workspaceSlug: String, repo: String, hash: String, path: String): ApiResult<List<RepoFile>> {
                    TODO("Not yet implemented")
                }

                override suspend fun getSourceFile(workspaceSlug: String, repo: String, hash: String, path: String): ApiResult<String> {
                    TODO("Not yet implemented")
                }

                override suspend fun createSnippet(title: String, filename: String, contents: String, private: Boolean): ApiResult<Unit> {
                    TODO("Not yet implemented")
                }

                override fun clear() {
                    TODO("Not yet implemented")
                }
            },
            buildConfigProvider = BuildConfigProviderImpl(),
            toaster = object : Toaster {
                override fun toast(message: String, duration: Int) {
                    TODO("Not yet implemented")
                }

                override fun toast(resId: Int, duration: Int) {
                    TODO("Not yet implemented")
                }
            },
            dispatcherProvider = object : DispatcherProvider {
                override val Default: CoroutineDispatcher
                    get() = TODO("Not yet implemented")
                override val IO: CoroutineDispatcher
                    get() = TODO("Not yet implemented")
                override val Main: CoroutineDispatcher
                    get() = TODO("Not yet implemented")
                override val Unconfined: CoroutineDispatcher
                    get() = TODO("Not yet implemented")
            }
        )
    )
}
