package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.colorResource
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
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepository
import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentType
import com.bottlerocketstudios.brarchitecture.data.model.EnvironmentConfig
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.ui.compose.fakes.ForceCrashLogicNoOp
import com.bottlerocketstudios.brarchitecture.ui.compose.fakes.ToasterNoOp
import com.google.android.material.composethemeadapter.MdcTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Screen that manages dev options (only accessible from internal or debug builds (aka non release production builds)
 *
 * As this is a non production screen, note that user facing strings are hardcoded and text styles are manually built up vs using/referencing xml styles.
 */
@Suppress("TooManyFunctions")
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
        }
    }

    @Composable
    private fun ScreenContent(viewModel: DevOptionsViewModel) {
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
    private fun FabLayout(onFabClick: () -> Unit) {
        FloatingActionButton(
            onClick = { onFabClick() },
            backgroundColor = colorResource(R.color.colorAccent),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_restart_black_24dp),
                    contentDescription = "Restart",
                    tint = Color.White
                )
            }
        )
    }

    @Composable
    private fun ScrollContent(viewModel: DevOptionsViewModel) {
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
                    CardLayout { EnvironmentCard(baseUrl = viewModel.baseUrl.collectAsState().value) }
                    CardDivider(MaterialTheme.colors.primary)
                }
                item {
                    CardLayout { AppInfoCard(applicationInfo = viewModel.applicationInfo) }
                    CardDivider(MaterialTheme.colors.primary)
                }
                item {
                    CardLayout { MiscFunctionalityCard(onForceCrashCtaClick = viewModel::onForceCrashCtaClicked) }
                    CardDivider(Color.Transparent)
                }
            }
        }
    }

    @Composable
    private fun BottomButton(buttonText: String, onClick: () -> Unit) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .padding(8.dp)
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
    private fun EnvironmentCard(baseUrl: String) {
        CardTitle(cardTitle = "Selected Environment Info")
        TitleValueRow(title = "Base URL", entryValue = baseUrl)
    }

    @Composable
    private fun AppInfoCard(applicationInfo: ApplicationInfo) {
        CardTitle(cardTitle = "App Info")
        TitleValueRow(title = "Version Name", entryValue = applicationInfo.appVersionName)
        TitleValueRow(title = "Version Code", entryValue = applicationInfo.appVersionCode)
        TitleValueRow(title = "Application ID", entryValue = applicationInfo.appId)
        TitleValueRow(title = "Build Identifier", entryValue = applicationInfo.buildIdentifier)
    }

    @Composable
    private fun MiscFunctionalityCard(onForceCrashCtaClick: () -> Unit) {
        CardTitle(cardTitle = "Misc Functionality")
        BottomButton(buttonText = "FORCE CRASH", onForceCrashCtaClick)
    }

    @Composable
    private fun CardDivider(dividerColor: Color) {
        Divider(
            color = dividerColor,
            thickness = 2.dp,
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
        )
    }

    @Composable
    private fun CardTitle(cardTitle: String) {
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
    private fun TitleValueRow(title: String, entryValue: String) {
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
    private fun CardLayout(content: @Composable () -> Unit) {
        Card(elevation = 4.dp) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 12.dp, bottom = 12.dp)
            ) {
                content()
            }
        }
    }

    // TODO this needs to be update when the next stable release comes out. New Menus
    @Composable
    @Suppress("LongMethod")
    private fun DropdownEnvMenu(viewModel: DevOptionsViewModel) {
        var expanded by remember { mutableStateOf(false) }
        val items = viewModel.environmentNames.collectAsState().value
        var selectedIndex by remember { mutableStateOf(viewModel.environmentSpinnerPosition) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            items[selectedIndex].let {
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
                items.forEachIndexed { index, s ->
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
        MdcTheme {
            Surface {
                ScreenContent(viewModel = viewModel)
            }
        }
    }

    @Preview(showBackground = true)
    @Suppress("EmptyFunctionBlock")
    @Composable
    private fun PreviewOuterScreenContent() {
        OuterScreenContent(
            // TODO: Find a better way to do this
            viewModel = DevOptionsViewModel(
                app = Application(),
                environmentRepository = object : EnvironmentRepository {
                    override val selectedConfig: EnvironmentConfig = EnvironmentConfig(EnvironmentType.PRODUCTION, "https://mock.com")
                    override val environments: List<EnvironmentConfig> =
                        listOf(EnvironmentConfig(EnvironmentType.PRODUCTION, "https://mock.com"), EnvironmentConfig(EnvironmentType.STG, "https://mock.com"))

                    override fun changeEnvironment(environmentType: EnvironmentType) {}
                },
                applicationInfoManager = object : ApplicationInfoManager {
                    override fun getApplicationInfo(): ApplicationInfo = ApplicationInfo("10.1.3", "1001030", "com.example.foo", "171")
                },
                forceCrashLogicImpl = ForceCrashLogicNoOp(),
                toaster = ToasterNoOp(),
            )
        )
    }
}
