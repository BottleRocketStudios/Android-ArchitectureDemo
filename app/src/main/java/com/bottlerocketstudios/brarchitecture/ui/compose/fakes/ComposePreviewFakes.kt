package com.bottlerocketstudios.brarchitecture.ui.compose.fakes

import com.bottlerocketstudios.brarchitecture.data.crashreporting.ForceCrashLogic
import com.bottlerocketstudios.brarchitecture.infrastructure.toast.Toaster

/**
 * Intended to contain all fakes needed for Compose `@Previews`. Defined here rather than per Preview function to keep code more DRY.
 * TODO: Create a lint rule (or some other solution) to restrict usage of these fakes to `@Preview` annotated functions.
 */

/** Intended for composable preview usage only */
@Suppress("EmptyFunctionBlock")
open class ToasterNoOp : Toaster {
    override fun toast(message: String, duration: Int) {}
    override fun toast(resId: Int, duration: Int) {}
}

/** Intended for composable preview usage only */
@Suppress("EmptyFunctionBlock")
open class ForceCrashLogicNoOp : ForceCrashLogic {
    override fun forceCrashOnMatch(input: String?) {}
    override fun forceCrashNow() {}
}
