package com.bottlerocketstudios.compose.devoptions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.resources.ArchitectureDemoTheme
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewAll
import com.bottlerocketstudios.compose.widgets.PrimaryButton
import com.bottlerocketstudios.launchpad.compose.bold
import com.bottlerocketstudios.launchpad.compose.light
import com.bottlerocketstudios.launchpad.compose.normal

@Composable
fun DevOptionsScreen(state: DevOptionsState) {
    DevOptionsScreenTheme {
        Scaffold(floatingActionButton = { FabLayout(state.onRestartCtaClick) }) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize().padding(paddingValues)
            ) {
                DropdownEnvMenu(state)
                ScrollContent(state)
            }
        }
    }
}

@Composable
fun DevOptionsScreenTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            onSurface = MaterialTheme.colors.onBackground,
        )
    ) {
        content()
    }
}

@Composable
private fun FabLayout(onFabClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onFabClick() },
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_restart_24),
                contentDescription = "Restart"
            )
        }
    )
}

@Composable
private fun ScrollContent(state: DevOptionsState) {
    Surface {
        LazyColumn(
            modifier = Modifier
                .padding(
                    start = Dimens.grid_1_5,
                    end = Dimens.grid_1_5
                )
                .fillMaxSize()
        ) {
            item {
                Spacer(Modifier.height(Dimens.grid_3))
                CardLayout { EnvironmentCard(baseUrl = state.baseUrl.value) }
                CardDivider(ArchitectureDemoTheme.colors.primary)
            }

            item {
                CardLayout { AppInfoCard(state = state) }
                CardDivider(ArchitectureDemoTheme.colors.primary)
            }

            item {
                CardLayout { MiscFunctionalityCard(onForceCrashCtaClick = state.onForceCrashCtaClicked) }
                Spacer(Modifier.height(Dimens.grid_3))
            }
        }
    }
}

@Composable
private fun EnvironmentCard(baseUrl: String) {
    CardTitle(cardTitle = "Selected Environment Info")
    TitleValueRow(title = "Base URL", entryValue = baseUrl)
}

@Composable
private fun AppInfoCard(state: DevOptionsState) {
    CardTitle(cardTitle = "App Info")
    TitleValueRow(title = "Version Name", entryValue = state.appVersionName)
    TitleValueRow(title = "Version Code", entryValue = state.appVersionCode)
    TitleValueRow(title = "Application ID", entryValue = state.appId)
    TitleValueRow(title = "Build Identifier", entryValue = state.buildIdentifier)
}

@Composable
private fun MiscFunctionalityCard(onForceCrashCtaClick: () -> Unit) {
    CardTitle(cardTitle = "Misc Functionality")
    PrimaryButton(
        buttonText = "FORCE CRASH",
        onClick = onForceCrashCtaClick,
        modifier = Modifier
            .padding(Dimens.grid_1)
            .fillMaxWidth()
    )
}

@Composable
private fun CardDivider(dividerColor: Color) {
    Divider(
        color = dividerColor,
        thickness = Dimens.grid_0_25,
        modifier = Modifier.padding(
            top = Dimens.grid_1_5,
            bottom = Dimens.grid_1_5
        )
    )
}

@Composable
private fun CardTitle(cardTitle: String) {
    Text(
        cardTitle,
        style = MaterialTheme.typography.h3.normal(),
        modifier = Modifier
            .padding(bottom = Dimens.grid_1)
            .fillMaxWidth()
    )
}

@Composable
private fun TitleValueRow(title: String, entryValue: String) {
    Text(
        title,
        style = MaterialTheme.typography.h3.bold(),
        modifier = Modifier
            .padding(
                top = Dimens.grid_1,
                bottom = Dimens.grid_1
            )
            .wrapContentHeight()
            .fillMaxWidth()
    )
    Text(
        entryValue,
        style = MaterialTheme.typography.h3.light(),
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    )
}

@Composable
private fun CardLayout(content: @Composable () -> Unit) {
    Card(elevation = Dimens.plane_3) {
        Column(
            modifier = Modifier
                .padding(
                    start = Dimens.grid_1,
                    end = Dimens.grid_1,
                    top = Dimens.grid_1_5,
                    bottom = Dimens.grid_1_5
                )
        ) {
            content()
        }
    }
}

// TODO this needs to be update when the next stable release comes out. New Menus
@Composable
private fun DropdownEnvMenu(state: DevOptionsState) {
    var expanded by remember { mutableStateOf(false) }
    val items = state.environmentNames.value
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
    ) {
        EnvironmentList(
            text = items[state.environmentSpinnerPosition.value],
            expanded = expanded
        ) {
            expanded = true
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    state.onEnvironmentChanged(index)
                }) {
                    Text(text = s, style = TextStyle(color = Color.Black))
                }
            }
        }
    }
}

@Composable
fun EnvironmentList(text: String, expanded: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .height(IntrinsicSize.Min)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
                .weight(1.0F)
                .padding(Dimens.grid_1_5)
        ) {
            Text(
                text = "Environment Switcher",
                style = MaterialTheme.typography.h5.normal(),
                color = if (expanded) MaterialTheme.colors.primary else Color.Unspecified
            )
            Text(
                text = text,
                style = MaterialTheme.typography.h4.normal()
            )
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
                .padding(end = Dimens.grid_1_5),
            tint = if (expanded) MaterialTheme.colors.primary else Color.DarkGray
        )
    }
}

@Preview
@PreviewAll
@Composable
private fun PreviewDevOptions() {
    Preview {
        val spinnerPosition = remember { mutableStateOf(0) }
        DevOptionsScreen(
            state = DevOptionsState(
                environmentNames = remember { mutableStateOf(listOf("POC", "TST", "PROD")) },
                environmentSpinnerPosition = spinnerPosition,
                baseUrl = remember { mutableStateOf("https://mock.com") },
                appVersionName = "10.1.3",
                appVersionCode = "1001030",
                appId = "com.example.foo",
                buildIdentifier = "171",
                onEnvironmentChanged = { index ->
                    spinnerPosition.value = index
                },
                onRestartCtaClick = { },
                onForceCrashCtaClicked = { }
            )
        )
    }
}
