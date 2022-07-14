package com.bottlerocketstudios.brarchitecture.ui.devoptions

import app.cash.turbine.test
import com.bottlerocketstudios.brarchitecture.domain.models.EnvironmentType
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.mocks.BASE_URL_PROD
import com.bottlerocketstudios.brarchitecture.test.mocks.BASE_URL_STG
import com.bottlerocketstudios.brarchitecture.test.mocks.MockApplicationInfoManager
import com.bottlerocketstudios.brarchitecture.test.mocks.MockEnvironmentRepository
import com.bottlerocketstudios.brarchitecture.test.mocks.MockEnvironmentRepository.ENVIRONMENT_PROD
import com.bottlerocketstudios.brarchitecture.test.mocks.MockEnvironmentRepository.mockEnvironmentRepository
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_BUILD_DEV
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_PACKAGE_NAME
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_VERSION_CODE
import com.bottlerocketstudios.brarchitecture.test.mocks.TEST_VERSION_NAME
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DevOptionsViewModelTest : BaseTest() {
    private lateinit var viewModel: DevOptionsViewModel

    // TODO: Test ApplicationInfoManagerImpl

    @Before
    fun setUp() {
        inlineKoinSingle { MockApplicationInfoManager.mockApplicationInfoManager }
        viewModel = DevOptionsViewModel()
    }

    @Test
    fun environmentNames_onViewModelInit_shouldReturnEnvironmentTypeList() = runBlocking {
        assertThat(viewModel.environmentNames.value)
            .isEqualTo(listOf(EnvironmentType.STG.shortName, EnvironmentType.PRODUCTION.shortName))
    }

    @Test
    fun environmentSpinnerPosition_onViewModelInit_shouldReturnIndexOfSelectedConfig() = runBlocking {
        assertThat(viewModel.environmentSpinnerPosition.value).isEqualTo(
            mockEnvironmentRepository.environments.indexOf(mockEnvironmentRepository.selectedConfig)
        )
    }

    @Test
    fun environmentSpinnerPosition_changePosition_shouldReturnNewIndex() = runBlocking {
        MockEnvironmentRepository._selectedConfig = MockEnvironmentRepository._environments[1]
        viewModel = DevOptionsViewModel()
        viewModel.environmentSpinnerPosition.test {
            assertThat(awaitItem())
                .isEqualTo(MockEnvironmentRepository._environments.indexOf(MockEnvironmentRepository._selectedConfig))
        }
    }

    @Test
    fun environmentSpinnerPosition_onEnvironmentChanged_positionShouldUpdate() = runBlocking {
        // Change environment to PROD
        MockEnvironmentRepository.newEnvironment.value = ENVIRONMENT_PROD
        // Call function to change environment
        viewModel.onEnvironmentChanged(
            MockEnvironmentRepository._environments.indexOf(
                MockEnvironmentRepository.newEnvironment.value
            )
        )
        // Assert that new spinner position is PROD and matches
        assertThat(viewModel.environmentSpinnerPosition.value).isEqualTo(
            mockEnvironmentRepository.environments.indexOf(ENVIRONMENT_PROD)
        )
    }

    @Test
    fun baseUrl_initWithSTG_shouldReturnStgBaseUrl() = runBlocking {
        assertThat(viewModel.baseUrl.value).isEqualTo(BASE_URL_STG)
    }

    @Test
    fun basUrl_initWithPROD_shouldReturnProdBaseUrl() = runBlocking {
        // Change init environment
        MockEnvironmentRepository._selectedConfig = ENVIRONMENT_PROD
        // Re "init" viewModel
        viewModel = DevOptionsViewModel()
        // Assert environment has changed
        assertThat(viewModel.baseUrl.value).isEqualTo(BASE_URL_PROD)
    }

    @Test
    fun baseUrl_onEnvironmentChanged_urlShouldUpdate() = runBlocking {
        // Change environment to PROD
        MockEnvironmentRepository.newEnvironment.value = ENVIRONMENT_PROD
        viewModel.onEnvironmentChanged(MockEnvironmentRepository._environments.indexOf(ENVIRONMENT_PROD))

        // Assert that baseUrl value has updated to PROD
        assertThat(viewModel.baseUrl.value).isEqualTo(BASE_URL_PROD)
    }

    @Test
    fun applicationInfo_onViewModelInit_appInfoShouldMatch() = runBlocking {
        // Multiple asserts to check contents; if even one assert fails, this test should fail
        assertThat(viewModel.applicationInfo.appVersionName).isEqualTo(TEST_VERSION_NAME)
        assertThat(viewModel.applicationInfo.appVersionCode).isEqualTo(TEST_VERSION_CODE.toString())
        assertThat(viewModel.applicationInfo.appId).isEqualTo(TEST_PACKAGE_NAME)
        assertThat(viewModel.applicationInfo.buildIdentifier).isEqualTo(TEST_BUILD_DEV)
    }
}
