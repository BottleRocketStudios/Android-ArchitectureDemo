package com.bottlerocketstudios.compose.devoptions

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bottlerocketstudios.compose.R

data class DevOptionsState(
    val environmentNames: State<List<String>>,
    val environmentSpinnerPosition: State<Int>,
    val baseUrl: State<String>,
    val appVersionName: String,
    val appVersionCode: String,
    val appId: String,
    val buildIdentifier: String,
    val onEnvironmentChanged: (Int) -> Unit,
    val onRestartCtaClick: () -> Unit,
    val onForceCrashCtaClicked: () -> Unit
)

@Composable
fun DevOptionsScreen(state: DevOptionsState) {
    Scaffold(floatingActionButton = { FabLayout(state.onRestartCtaClick) }) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            DropdownEnvMenu(state)
            ScrollContent(state)
        }
    }
}

@Composable
private fun FabLayout(onFabClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onFabClick() },
        backgroundColor = MaterialTheme.colors.secondary,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_restart_24),
                contentDescription = "Restart",
                tint = Color.White
            )
        }
    )
}

@Composable
private fun ScrollContent(state: DevOptionsState) {
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
                CardLayout { EnvironmentCard(baseUrl = state.baseUrl.value) }
                CardDivider(MaterialTheme.colors.primary)
            }
            item {
                CardLayout { AppInfoCard(state = state) }
                CardDivider(MaterialTheme.colors.primary)
            }
            item {
                CardLayout { MiscFunctionalityCard(onForceCrashCtaClick = state.onForceCrashCtaClicked) }
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
private fun DropdownEnvMenu(state: DevOptionsState) {
    var expanded by remember { mutableStateOf(false) }
    val items = state.environmentNames.value
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
    ) {
        items[state.environmentSpinnerPosition.value].let {
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
                    expanded = false
                    state.onEnvironmentChanged(index)
                }) {
                    Text(text = s, style = TextStyle(color = Color.Black))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewOuterScreenContent() {
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
