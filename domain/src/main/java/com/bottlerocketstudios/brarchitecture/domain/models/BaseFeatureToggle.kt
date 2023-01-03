package com.bottlerocketstudios.brarchitecture.domain.models

interface BaseFeatureToggle<T : Any?> {
    /** Name of the feature toggle. Should be unique amongst toggles. */
    val name: String

    /** Current value of the feature toggle. */
    var value: T

    /**
     * Default value of the toggle as defined by the backing data source.
     * Can be used to understand if a feature toggle's value has been overridden locally.
     */
    var defaultValue: T

    /**
     * When true, changes to a feature toggles value will require the app to be restarted to take effect. Otherwise,
     *  the app would be in an invalid state and behave unexpectedly).
     * When false, the feature toggle can be modified dynamically and the app can still be used/operated without a restart.
     */
    val requireRestart: Boolean
}

inline fun <reified T> BaseFeatureToggle<T>.isValueOverridden(): Boolean = value != defaultValue
