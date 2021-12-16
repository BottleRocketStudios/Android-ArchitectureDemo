package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.buildconfig.BuildConfigProviderImpl
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogicImpl
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepository
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentType
import com.bottlerocketstudios.brarchitecture.data.model.EnvironmentConfig
import com.bottlerocketstudios.brarchitecture.infrastructure.coroutine.DispatcherProvider
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.compose.ArchitectureDemoTheme
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Screen that manages dev options (only accessible from internal or debug builds (aka non release production builds)
 */
class DevOptionsFragment : BaseFragment<DevOptionsViewModel>() {
    override val fragmentViewModel: DevOptionsViewModel by viewModel()
    private val buildConfigProvider by inject<BuildConfigProvider>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Only debug/internal builds allowed to show this screen. Immediately close if somehow launched on prod release build.
        if (buildConfigProvider.isProductionReleaseBuild) {
            // NOTE: Special case usage of findNavController
            findNavController().popBackStack()
            return
        }
    }

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

            fragmentViewModel.messageToUser.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Composable
    fun ScreenContent(viewModel: DevOptionsViewModel) {
        DropdownEnvMenu(viewModel)
    }

    //TODO this needs to be update when the next stable release comes out. New Menus
    @Composable
    fun DropdownEnvMenu(viewModel: DevOptionsViewModel) {
        var expanded by remember { mutableStateOf(false) }
        val items = viewModel.environmentNames.value
        var selectedIndex by remember { mutableStateOf(viewModel.environmentSpinnerPosition) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            items?.get(selectedIndex)?.let {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { expanded = true })
                        .height(IntrinsicSize.Min)
                        .background(
                            Color.LightGray
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(Alignment.CenterVertically)
                            .weight(1.0F)
                            .padding(12.dp)
                    ) {
                        Text("Environment Switcher", style = TextStyle(color = if (expanded) MaterialTheme.colors.primary else Color.DarkGray, fontSize = 12.sp))
                        Text(it, style = TextStyle(color = Color.Black, fontSize = 14.sp))
                    }

                    val displayIcon: Painter = painterResource(
                        id = R.drawable.ic_arrow_drop_down_24
                    )
                    Icon(
                        painter = displayIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .fillMaxHeight()
                            .padding(end = 12.dp),
                        tint = if (expanded) MaterialTheme.colors.primary else Color.DarkGray
                    )
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White
                    )
            ) {
                items?.forEachIndexed { index, s ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        expanded = false
                        viewModel.onEnvironmentChanged(selectedIndex)
                    }) {
                        Text(text = s, style = TextStyle(color = Color.Black))
                    }
                }
            }
        }
    }

    @Composable
    private fun OuterScreenContent(viewModel: DevOptionsViewModel) {
        ArchitectureDemoTheme {
            Surface {
                ScreenContent(viewModel = viewModel)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun PreviewOuterScreenContent() {
        OuterScreenContent(
            // TODO: Find a better way to do this (might be using Koin field injection in the ViewModel rather than constructor injection)
            viewModel = DevOptionsViewModel(
                app = Application(),
                buildConfigProvider = BuildConfigProviderImpl(),
                dispatcherProvider = object : DispatcherProvider {
                    override val Default: CoroutineDispatcher
                        get() = TODO("Not yet implemented")
                    override val IO: CoroutineDispatcher
                        get() = TODO("Not yet implemented")
                    override val Main: CoroutineDispatcher
                        get() = TODO("Not yet implemented")
                    override val Unconfined: CoroutineDispatcher
                        get() = TODO("Not yet implemented")
                },
                environmentRepository = object : EnvironmentRepository {
                    override val selectedConfig: EnvironmentConfig
                        get() = TODO("Not yet implemented")
                    override val environments: List<EnvironmentConfig>
                        get() = TODO("Not yet implemented")

                    override fun changeEnvironment(environmentType: EnvironmentType) {
                        TODO("Not yet implemented")
                    }
                },
                forceCrashLogicImpl = ForceCrashLogicImpl(buildConfigProvider)
            )
        )
    }
}
