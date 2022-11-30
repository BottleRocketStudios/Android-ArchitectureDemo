package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.bottlerocketstudios.brarchitecture.domain.models.FeatureToggle
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FeatureToggleTest : BaseTest() {

    @Test
    fun featureToggle_defaultFields_whenDefaultConstructor() {
        val featureToggle = FeatureToggle(null, null, null, null)
        assertThat(featureToggle.name).isNull()
        assertThat(featureToggle.value).isNull()
        assertThat(featureToggle.defaultValue).isNull()
        assertThat(featureToggle.requireRestart).isNull()
    }
}
