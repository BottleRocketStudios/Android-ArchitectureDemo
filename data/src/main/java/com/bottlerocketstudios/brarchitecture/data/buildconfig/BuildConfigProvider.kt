package com.bottlerocketstudios.brarchitecture.data.buildconfig

/** Abstracts values from the application generated BuildConfig to be accessible anywhere and improve testability */
interface BuildConfigProvider {

    /** True when this is a developer or debug type build. ![isDebugBuild] == [isProductionReleaseBuild] */
    val isDebugBuild: Boolean

    /** True when this is the release variant.  ![isProductionReleaseBuild] == [isDebugBuild]  */
    val isProductionReleaseBuild: Boolean

    /**
     * A string to help identify the build between dev/QA, intended to be shown in DevOptions UI.
     *
     * #### Examples
     * * CI debug:    "debug-feature__update-version-name-and-apk-name-build-350-3d7f6b4-2020-05-14"
     * * local debug: "debug-feature__update-version-name-and-apk-name-dev_build-3d7f6b4-2020-05-14"
     * * release:     ""
     *
     * See BuildInfoManager.createBuildFingerprint(...)
     */
    val buildIdentifier: String
}
