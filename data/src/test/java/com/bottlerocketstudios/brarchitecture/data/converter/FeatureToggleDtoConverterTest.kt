package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.FeatureToggleDto
import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FeatureToggleDtoConverterTest : BaseTest() {

    @Test
    fun featureToggle_shouldCreateFeatureToggle_whenConvertToFeatureToggle() {
        val testToggleDto = FeatureToggleDto(name = "TEST", value = true, defaultValue = true, requireRestart = true)
        val testToggle = testToggleDto.toFeatureToggle()
        assertThat(testToggle.name).isEqualTo(testToggleDto.name)
        assertThat(testToggle.value).isEqualTo(testToggleDto.value)
        assertThat(testToggle.defaultValue).isEqualTo(testToggleDto.defaultValue)
        assertThat(testToggle.requireRestart).isEqualTo(testToggleDto.requireRestart)
    }

    @Test
    fun featureToggle_shouldCreateFeatureToggle_whenConvertToFeatureToggleNameEmpty() {
        val testToggleDto = FeatureToggleDto(name = "", value = true, defaultValue = true, requireRestart = true)
        val testToggle = testToggleDto.toFeatureToggle()
        assertThat(testToggle.name).isEmpty()
        assertThat(testToggle.value).isEqualTo(testToggleDto.value)
        assertThat(testToggle.defaultValue).isEqualTo(testToggleDto.defaultValue)
        assertThat(testToggle.requireRestart).isEqualTo(testToggleDto.requireRestart)
    }
}
