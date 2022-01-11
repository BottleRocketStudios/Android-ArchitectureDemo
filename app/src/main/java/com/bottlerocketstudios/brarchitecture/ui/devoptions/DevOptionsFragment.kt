package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.bottlerocketstudios.brarchitecture.R
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
        } else {
            setAppPackageValues()
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

    private fun setAppPackageValues() {
        fragmentViewModel.appVersionName.value = activity?.packageManager?.getPackageInfo(activity?.packageName ?: "", 0)?.versionName ?: ""
        fragmentViewModel.appVersionCode.value = activity?.packageManager?.getPackageInfo(activity?.packageName ?: "", 0)?.versionCode.toString()
        fragmentViewModel.appId.value = activity?.packageName
        fragmentViewModel.buildIdentifier.value = buildConfigProvider.buildIdentifier
    }

    @Composable
    fun ScreenContent(viewModel: DevOptionsViewModel) {
        Scaffold(floatingActionButton = { FabLayout(viewModel::onRestartCtaClick) }) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                DropdownEnvMenu(viewModel)
                ScrollContent(viewModel)
            }
        }
    }

    @Composable
    fun FabLayout(onClick: () -> Unit) {
        FloatingActionButton(
            onClick = { onClick() },
            backgroundColor = Color(31, 173, 168),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_restart_black_24dp),
                    contentDescription = "Restart",
                    tint = Color.White
                )
            })
    }

    @Composable
    fun ScrollContent(viewModel: DevOptionsViewModel) {
        Column {
            LazyColumn(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                item {
                    CardDivider(Color.Transparent)
                    CardLayout { CardOne(viewModel = viewModel) }
                    CardDivider(MaterialTheme.colors.primary)
                }
                item {
                    CardLayout { CardTwo(viewModel = viewModel) }
                    CardDivider(MaterialTheme.colors.primary)
                }
                item {
                    CardLayout { CardThree() }
                    CardDivider(Color.Transparent)
                }
            }
            BottomButton(buttonText = "FORCE CRASH", viewModel::onForceCrashCtaClicked)
        }
    }

    @Composable
    fun BottomButton(buttonText: String, onClick: () -> Unit) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, bottom = 84.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.primary
            ),
            shape = RoundedCornerShape(7.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            Text(
                text = buttonText,
                style = TextStyle(color = Color.White, fontSize = 14.sp)
            )
        }
    }

    @Composable
    fun CardOne(viewModel: DevOptionsViewModel) {
        CardTitle(cardTitle = "Selected Environment Info")
        CardEntry(title = "Base URL", entryValue = viewModel.baseUrl.value ?: "")
    }

    @Composable
    fun CardTwo(viewModel: DevOptionsViewModel) {
        CardTitle(cardTitle = "App Info")
        CardEntry(title = "Version Name", entryValue = viewModel.appVersionName.value ?: "")
        CardEntry(title = "Version Code", entryValue = viewModel.appVersionCode.value ?: "")
        CardEntry(title = "Application ID", entryValue = viewModel.appId.value ?: "")
        CardEntry(title = "Build Identifier", entryValue = viewModel.buildIdentifier.value ?: "")
    }

    @Composable
    fun CardThree() {
        CardTitle(cardTitle = "Misc Functionality")
    }

    @Composable
    fun CardDivider(dividerColor: Color) {
        Divider(
            color = dividerColor,
            thickness = 2.dp,
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
        )
    }

    @Composable
    fun CardTitle(cardTitle: String) {
        Text(
            cardTitle,
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .wrapContentHeight()
                .fillMaxWidth()
        )
    }

    @Composable
    fun CardEntry(title: String, entryValue: String) {
        Text(
            title,
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .wrapContentHeight()
                .fillMaxWidth()
        )
        Text(
            entryValue,
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light
            ),
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        )
    }

    @Composable
    fun CardLayout(content: @Composable () -> Unit) {
        Card(elevation = 4.dp) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 12.dp, bottom = 12.dp)
            ) {
                content()
            }
        }
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
                        get() = EnvironmentConfig(EnvironmentType.PRODUCTION, "https://mock.com")
                    override val environments: List<EnvironmentConfig>
                        get() = listOf(EnvironmentConfig(EnvironmentType.PRODUCTION, "https://mock.com"), EnvironmentConfig(EnvironmentType.STG, "https://mock.com"))

                    override fun changeEnvironment(environmentType: EnvironmentType) {
                        TODO("Not yet implemented")
                    }
                },
                forceCrashLogicImpl = ForceCrashLogicImpl(object : BuildConfigProvider {
                    override val isDebugOrInternalBuild: Boolean
                        get() = true
                    override val isProductionReleaseBuild: Boolean
                        get() = false
                    override val buildIdentifier: String
                        get() = "11111111"
                })
            )
        )
    }
}
