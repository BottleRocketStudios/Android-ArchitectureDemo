package com.bottlerocketstudios.brarchitecture.test.mocks

import com.bottlerocketstudios.brarchitecture.data.environment.EnvironmentRepository
import com.bottlerocketstudios.brarchitecture.domain.models.EnvironmentConfig
import com.bottlerocketstudios.brarchitecture.domain.models.EnvironmentType
import kotlinx.coroutines.flow.MutableStateFlow
import org.mockito.kotlin.mock

const val BASE_URL_STG = "test_stg_url"
const val BASE_URL_PROD = "test_prod_url"

object MockEnvironmentRepository {
    val ENVIRONMENT_STG = EnvironmentConfig(EnvironmentType.STG, BASE_URL_STG)
    val ENVIRONMENT_PROD = EnvironmentConfig(EnvironmentType.PRODUCTION, BASE_URL_PROD)

    var _selectedConfig: EnvironmentConfig = ENVIRONMENT_STG
    var _environments = listOf(ENVIRONMENT_STG, ENVIRONMENT_PROD)

    val newEnvironment = MutableStateFlow(ENVIRONMENT_STG)

    val mockEnvironmentRepository: EnvironmentRepository = mock {
        on { selectedConfig }.then { _selectedConfig }
        on { environments }.then { _environments }

        on { changeEnvironment(newEnvironment.value.environmentType) }.then {
            _selectedConfig = newEnvironment.value
            Unit
        }
    }
}
