package com.bottlerocketstudios.compose.featuretoggles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import com.bottlerocketstudios.brarchitecture.domain.models.isValueOverridden
import com.bottlerocketstudios.compose.resources.Dimens
import com.bottlerocketstudios.compose.util.Preview
import com.bottlerocketstudios.compose.util.PreviewAll
import com.bottlerocketstudios.compose.widgets.PrimaryButton
import com.bottlerocketstudios.launchpad.compose.bold
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun FeatureToggleScreen(state: FeatureToggleState) {

    val toggleList = state.featureToggles.collectAsState().value

    FeatureToggleScreenTheme {
        Column {
            Card(modifier = Modifier.padding(Dimens.grid_2)) {
                LazyColumn(modifier = Modifier.padding(Dimens.grid_2)) {
                    items(toggleList.filterIsInstance<FeatureToggle.ToggleValueBoolean>()) { toggle ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(toggle.name, style = MaterialTheme.typography.h5.bold(), modifier = Modifier.fillMaxHeight())
                                if (toggle.isValueOverridden()) {
                                    Text(text = "Default Overridden", style = MaterialTheme.typography.h6)
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Checkbox(checked = toggle.value, onCheckedChange = {
                                    toggle.value = !toggle.value
                                    state.updateFeatureToggleValue(toggle)
                                })
                            }
                        }
                    }
                    items(toggleList.filterIsInstance<FeatureToggle.ToggleValueEnum>()) { toggle ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = Dimens.grid_1, bottom = Dimens.grid_1)) {
                            Column(
                                modifier = Modifier
                            ) {
                                Text(toggle.name, style = MaterialTheme.typography.h5.bold())
                                if (toggle.isValueOverridden()) {
                                    Text("Default Overridden", style = MaterialTheme.typography.h6)
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(toggle.value.toString())
                            }
                        }
                    }
                }
            }

            PrimaryButton(
                buttonText = "RESET FEATURE TOGGLES",
                onClick = { state.onResetFeatureTogglesClick() },
                modifier = Modifier
                    .padding(Dimens.grid_1)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun FeatureToggleScreenTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            onSurface = MaterialTheme.colors.onBackground,
        )
    ) {
        content()
    }
}
@Preview
@PreviewAll
@Composable
private fun PreviewFeatureToggles() {
    Preview {
        FeatureToggleScreen(
            state = FeatureToggleState(
                featureToggles = MutableStateFlow(
                    listOf(
                        FeatureToggle.ToggleValueBoolean(
                            name = "L",
                            value = false,
                            defaultValue = true,
                            requireRestart = false
                        ) as FeatureToggle,
                        FeatureToggle.ToggleValueBoolean(
                            name = "K",
                            value = true,
                            defaultValue = true,
                            requireRestart = true
                        ) as FeatureToggle,
                        FeatureToggle.ToggleValueEnum(
                            name = "PROGRESS",
                            value = FeatureToggle.ToggleValueEnum.ToggleEnum.WEBVIEW,
                            defaultValue = FeatureToggle.ToggleValueEnum.ToggleEnum.EXTERNAL_BROWSER,
                            requireRestart = false
                        ) as FeatureToggle
                    ).toSet()
                ),
                onResetFeatureTogglesClick = {},
                updateFeatureToggleValue = {}
            )
        )
    }
}
