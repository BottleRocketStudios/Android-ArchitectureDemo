package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import com.bottlerocketstudios.brarchitecture.domain.models.isValueOverridden
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FeatureToggleTest : BaseTest() {

    @Test
    fun featureToggle_defaultFields_whenDefaultConstructor() {
        val featureToggle = FeatureToggle.ToggleValueBoolean(name = "SHOW_PULL_REQUESTS", value = true, defaultValue = true, requireRestart = false)
        assertThat(featureToggle.name).isNotNull()
        assertThat(featureToggle.value).isNotNull()
        assertThat(featureToggle.defaultValue).isNotNull()
        assertThat(featureToggle.requireRestart).isNotNull()
    }

    @Test
    fun featureToggle_toggleOverridden_false() {
        val featureToggle = FeatureToggle.ToggleValueBoolean(name = "SHOW_PULL_REQUESTS", value = true, defaultValue = true, requireRestart = true)
        assertThat(featureToggle.isValueOverridden()).isFalse()
    }

    @Test
    fun featureToggle_toggleOverridden_true() {
        val featureToggle = FeatureToggle.ToggleValueBoolean(name = "SHOW_PULL_REQUESTS", value = false, defaultValue = true, requireRestart = true)
        assertThat(featureToggle.isValueOverridden()).isTrue()
    }
}
