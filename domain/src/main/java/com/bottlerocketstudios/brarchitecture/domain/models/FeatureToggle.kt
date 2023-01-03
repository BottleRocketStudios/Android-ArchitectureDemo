package com.bottlerocketstudios.brarchitecture.domain.models

sealed class FeatureToggle {
    data class ToggleValueBoolean(
        override val name: String,
        override var value: Boolean,
        override var defaultValue: Boolean,
        override val requireRestart: Boolean
    ) : BaseFeatureToggle<Boolean>, FeatureToggle()

    data class ToggleValueEnum(
        override val name: String,
        override var value: ToggleEnum,
        override var defaultValue: ToggleEnum,
        override val requireRestart: Boolean
    ) : BaseFeatureToggle<ToggleValueEnum.ToggleEnum>, FeatureToggle() {
        enum class ToggleEnum {
            WEBVIEW,
            CHROME_CUSTOM_TAB,
            EXTERNAL_BROWSER
        }
    }
}
