package com.bottlerocketstudios.brarchitecture.data.environment

import android.content.SharedPreferences
import com.bottlerocketstudios.brarchitecture.domain.models.EnvironmentConfig
import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.bottlerocketstudios.brarchitecture.data.test.mocks.MockBuildConfigProviders
import com.bottlerocketstudios.brarchitecture.domain.models.EnvironmentType
import com.google.common.truth.Truth.assertThat
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.junit.Test

class EnvironmentRepositoryImplTest : BaseTest() {

    @Suppress("VariableNaming") // constant
    private val ENVIRONMENT_STG = EnvironmentConfig(EnvironmentType.STG, "https://REPLACE-THIS-HARDCODED-URL.STAGE.CLIENT.com")
    @Suppress("VariableNaming") // constant
    private val ENVIRONMENT_PROD = EnvironmentConfig(EnvironmentType.PRODUCTION, "https://REPLACE-THIS-HARDCODED-URL.PROD.CLIENT.com")

    @Suppress("VariableNaming") // constant
    private val DEV_ENVIRONMENT_CONFIGS = listOf(ENVIRONMENT_STG, ENVIRONMENT_PROD)
    @Suppress("VariableNaming") // constant
    private val PROD_ENVIRONMENT_CONFIGS = listOf(ENVIRONMENT_PROD)

    @Test
    fun getEnvironments_devBuild_matchesExpectedList() {
        val sut = EnvironmentRepositoryImpl(mock {}, MockBuildConfigProviders.DEV)
        assertThat(sut.environments).isEqualTo(DEV_ENVIRONMENT_CONFIGS)
    }

    @Test
    fun getEnvironments_prodReleaseBuild_matchesExpectedList() {
        val sut = EnvironmentRepositoryImpl(mock {}, MockBuildConfigProviders.PROD_RELEASE)
        assertThat(sut.environments).isEqualTo(PROD_ENVIRONMENT_CONFIGS)
    }

    @Test
    fun getSelectedConfig_prodReleaseBuild_alwaysReturnProdEnvironment() {
        val sut = EnvironmentRepositoryImpl(
            sharedPrefs = mock {
                on { getString(any(), any()) } doReturn "env_stage"
            },
            buildConfigProvider = MockBuildConfigProviders.PROD_RELEASE
        )
        assertThat(sut.selectedConfig).isEqualTo(ENVIRONMENT_PROD)
    }

    @Test
    fun getSelectedConfig_devBuildWithStageSelected_returnsStage() {
        val sut = EnvironmentRepositoryImpl(
            sharedPrefs = mock {
                on { getString(any(), any()) } doReturn "env_stage"
            },
            buildConfigProvider = MockBuildConfigProviders.DEV
        )
        assertThat(sut.selectedConfig).isEqualTo(ENVIRONMENT_STG)
    }

    @Test
    fun getSelectedConfig_devBuildNothingSelected_returnsFallback() {
        val sut = EnvironmentRepositoryImpl(
            sharedPrefs = mock {
                on { getString(any(), any()) } doReturn ""
            },
            buildConfigProvider = MockBuildConfigProviders.DEV
        )
        assertThat(sut.selectedConfig).isEqualTo(ENVIRONMENT_STG)
    }

    @Test
    fun getSelectedConfig_devBuildInvalidSharedPrefsValue_returnsFallback() {
        val sut = EnvironmentRepositoryImpl(
            sharedPrefs = mock {
                on { getString(any(), any()) } doReturn "asfsdf"
            },
            buildConfigProvider = MockBuildConfigProviders.DEV
        )
        assertThat(sut.selectedConfig).isEqualTo(ENVIRONMENT_STG)
    }

    @Test
    fun getSelectedConfig_devBuildProductionSelected_returnsProduction() {
        val sut = EnvironmentRepositoryImpl(
            sharedPrefs = mock {
                on { getString(any(), any()) } doReturn "env_production"
            },
            buildConfigProvider = MockBuildConfigProviders.DEV
        )
        assertThat(sut.selectedConfig).isEqualTo(ENVIRONMENT_PROD)
    }

    @Test
    fun changeEnvironment_prodReleaseBuildChangeToStage_noOp() {
        val mockSharedPreferencesEditor = mock<SharedPreferences.Editor> { editor ->
            on { putString(any(), any()) } doAnswer { editor }
        }
        val mockSharedPrefs = mock<SharedPreferences> {
            on { edit() } doReturn mockSharedPreferencesEditor
        }
        val sut = EnvironmentRepositoryImpl(mockSharedPrefs, MockBuildConfigProviders.PROD_RELEASE)
        sut.changeEnvironment(EnvironmentType.STG)
        verify(mockSharedPreferencesEditor, never()).putString(any(), any())
    }

    @Test
    fun changeEnvironment_devBuildChangeToStage_changedToStage() {
        val mockSharedPreferencesEditor = mock<SharedPreferences.Editor> { editor ->
            on { putString(any(), any()) } doAnswer { editor }
        }
        val mockSharedPrefs = mock<SharedPreferences> {
            on { edit() } doReturn mockSharedPreferencesEditor
        }
        val sut = EnvironmentRepositoryImpl(mockSharedPrefs, MockBuildConfigProviders.DEV)

        sut.changeEnvironment(EnvironmentType.STG)
        val keyCaptor = argumentCaptor<String>()
        val valueCaptor = argumentCaptor<String>()
        verify(mockSharedPreferencesEditor, times(1)).putString(keyCaptor.capture(), valueCaptor.capture())
        assertThat(keyCaptor.lastValue).isEqualTo("selected_environment_type")
        assertThat(valueCaptor.lastValue).isEqualTo("env_stage")
    }
}
