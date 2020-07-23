package com.bottlerocketstudios.brarchitecture.data.crashreporting

/** Helper to force crashes in different scenarios. Intended to be used by DEV/QA to validate crash reporting library is configured properly. */
interface ForceCrashLogic {
    /**
     * Special logic for testing crash reports by throwing a [RuntimeException] on a matching [input]
     *
     * @throws RuntimeException
     */
    fun forceCrashOnMatch(input: String?)

    /**
     * Only expected to be used by the dev options code! No-op if called on production release build.
     *
     * @throws RuntimeException
     */
    fun forceCrashNow()
}
