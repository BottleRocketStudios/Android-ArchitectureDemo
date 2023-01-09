package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.FeatureToggleBooleanDto
import com.bottlerocketstudios.brarchitecture.data.model.FeatureToggleDto
import com.bottlerocketstudios.brarchitecture.data.model.FeatureToggleStringDto
import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FeatureToggleDtoConverterTest : BaseTest() {

    @Test
    fun featureToggleDto_shouldCreateListOfBooleanAndEnum_whenConvertToFeatureToggle() {
        val booleansDto = listOf(
            FeatureToggleBooleanDto(name = "SHOW_SNIPPETS", value = true, defaultValue = true, requireRestart = true),
            FeatureToggleBooleanDto(name = "SHOW_PULL_REQUESTS", value = true, defaultValue = true, requireRestart = true)
        )
        val stringsDto = listOf(FeatureToggleStringDto(name = "STRING_VALUE", value = "WEBVIEW", defaultValue = "EXTERNAL_BROWSER", requireRestart = true))
        val overallToggle = FeatureToggleDto(booleanFlags = booleansDto, stringFlags = stringsDto)
        assertThat(overallToggle.booleanFlags.first().toFeatureToggle()).isInstanceOf(FeatureToggle.ToggleValueBoolean::class.java)
        assertThat(overallToggle.stringFlags.first().toFeatureToggle()).isInstanceOf(FeatureToggle.ToggleValueEnum::class.java)
    }

    @Test
    fun featureToggle_shouldCreateFeatureToggle_whenConvertToFeatureToggleBoolean() {
        val booleansDto = listOf(
            FeatureToggleBooleanDto(name = "SHOW_SNIPPETS", value = true, defaultValue = true, requireRestart = true),
            FeatureToggleBooleanDto(name = "SHOW_PULL_REQUESTS", value = true, defaultValue = true, requireRestart = true)
        )
        val overallToggle = FeatureToggleDto(booleanFlags = booleansDto, stringFlags = listOf())
        val testToggleBooleanDto = overallToggle.booleanFlags.first()
        val testToggleBoolean = testToggleBooleanDto.toFeatureToggle() as FeatureToggle.ToggleValueBoolean
        assertThat(testToggleBoolean.name).isEqualTo(testToggleBooleanDto.name)
        assertThat(testToggleBoolean.value).isEqualTo(testToggleBooleanDto.value)
        assertThat(testToggleBoolean.defaultValue).isEqualTo(testToggleBooleanDto.defaultValue)
        assertThat(testToggleBoolean.requireRestart).isEqualTo(testToggleBooleanDto.requireRestart)
    }

    @Test
    fun featureToggle_shouldCreateFeatureToggle_whenConvertToFeatureToggleEnum() {
        val stringsDto = listOf(FeatureToggleStringDto(name = "STRING_VALUE", value = "WEBVIEW", defaultValue = "EXTERNAL_BROWSER", requireRestart = true))
        val overallToggle = FeatureToggleDto(booleanFlags = listOf(), stringFlags = stringsDto)
        val testToggleStringDto = overallToggle.stringFlags.first()
        val testToggleString = testToggleStringDto.toFeatureToggle() as FeatureToggle.ToggleValueEnum
        assertThat(testToggleString.name).isEqualTo(testToggleStringDto.name)
        assertThat(testToggleString.value).isEqualTo(FeatureToggle.ToggleValueEnum.ToggleEnum.valueOf(testToggleStringDto.value))
        assertThat(testToggleString.defaultValue).isEqualTo(FeatureToggle.ToggleValueEnum.ToggleEnum.valueOf(testToggleStringDto.defaultValue))
        assertThat(testToggleString.requireRestart).isEqualTo(testToggleStringDto.requireRestart)
    }

    @Test
    fun featureToggle_shouldCreateFeatureToggle_whenConvertToFeatureToggleNameEmpty() {
        val overallDto = FeatureToggleDto(booleanFlags = listOf(FeatureToggleBooleanDto(name = "", value = false, defaultValue = true, requireRestart = true)), stringFlags = listOf())
        val testToggleDto = overallDto.booleanFlags.first()
        val testToggle = overallDto.booleanFlags.first().toFeatureToggle() as FeatureToggle.ToggleValueBoolean
        assertThat(testToggle.name).isEmpty()
        assertThat(testToggle.value).isEqualTo(testToggleDto.value)
        assertThat(testToggle.defaultValue).isEqualTo(testToggleDto.defaultValue)
        assertThat(testToggle.requireRestart).isEqualTo(testToggleDto.requireRestart)
    }

    @Test
    fun featureToggle_enumValueShouldMatchStringValue_onConvertToFeatureToggle() {
        val testToggleDto = FeatureToggleStringDto(name = "STRING_VALUE", value = "WEBVIEW", defaultValue = "EXTERNAL_BROWSER", requireRestart = false)
        val testToggle = testToggleDto.toFeatureToggle()
        assertThat((testToggle as FeatureToggle.ToggleValueEnum).value).isEqualTo(FeatureToggle.ToggleValueEnum.ToggleEnum.WEBVIEW)
    }
}
