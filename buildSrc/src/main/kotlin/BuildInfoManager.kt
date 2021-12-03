import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/** Represents build info needed by [BuildInfoManager] */
data class BuildInfoInput(
    val appVersion: AppVersion,
    /** Prefix used for apk name */
    val brandName: String,
    /** Name of the variant released on the Play Store. */
    val productionReleaseVariantName: String,
    val rootProjectDir: File
)

/** Provides functions to retrieve appropriate naming and versioning values per build variant. */
object BuildInfoManager {
    /** Value set when calling [initialize] */
    private lateinit var input: BuildInfoInput

    @Suppress("MemberVisibilityCanBePrivate") // no, it must be visible as it is used in the build.gradle.kts files
    /** Version of the app (version name and code derived from this value). */
    val APP_VERSION: AppVersion by lazy { input.appVersion }

    /** Build number set as an environment variable on the build server (or empty string for local builds) */
    private val BUILD_NUMBER: String
        get() = System.getenv("BUILD_NUMBER").orEmpty() // using get() to allow value to be read dynamically if it was to change (primarily during local machine task execution testing)

    /** True if this build is running an a continuous integration server (ie, Jenkins). False if running on a local dev machine. */
    private val IS_CI: Boolean
        get() = determineIfCi() // using get() to allow value to be read dynamically if it was to change (primarily during local machine task execution testing)

    /** True if apk/aab names should be overridden. Otherwise false */
    fun shouldOverrideApkAndAabNames(): Boolean = IS_CI
    /** True if version name should be overridden. Otherwise false */
    private fun shouldOverrideVersionName(variantName: String): Boolean = IS_CI && !isProductionReleaseVariant(variantName)

    @Suppress("MemberVisibilityCanBePrivate") // no, it must be visible as it is used in the build.gradle.kts files
    /** Call prior to android block definition in app build gradle. */
    fun initialize(buildInfoInput: BuildInfoInput) {
        if (!::input.isInitialized) {
            this.input = buildInfoInput
            logBuildInfo()
        } else {
            // Likely occurs due to gradle buildCache causing this object to already be initialized
            println("[initialize] already initialized")
            logBuildInfo()
        }
    }

    /** Creates BUILD_IDENTIFIER (accessible in code via BuildConfig.BUILD_IDENTIFIER). Call inside [applicationVariants.all] block */
    fun createBuildIdentifier(variant: ApplicationVariant) {
        println("[applicationVariants ${variant.name}] versionName: ${variant.versionName}")
        val buildFingerprint = createBuildFingerprint(variant.name)
        // Write to BUILD_IDENTIFIER to be used within the app's code (dev screen UI)
        variant.buildConfigField("String", "BUILD_IDENTIFIER", "\"$buildFingerprint\"")
        println("[applicationVariants ${variant.name}] buildFingerprint: '$buildFingerprint'")
    }

    /** Updates version name and apk name when appropriate. Call inside applicationVariant.outputs.all block (within the [applicationVariants.all] block) */
    fun modifyVersionNameAndApkName(variant: ApplicationVariant, output: BaseVariantOutput) {
        // ApkVariantOutput provides setVersionNameOverride and setOutputFileName
        // https://android.googlesource.com/platform/tools/base/+/studio-master-dev/build-system/gradle-core/src/main/java/com/android/build/gradle/api/ApkVariantOutput.java
        // Initial finding pointing out the setVersionNameOverride usage: https://stackoverflow.com/a/47053539/201939
        val apkVariantOutput: ApkVariantOutput = output as ApkVariantOutput

        // Don't change apk name for non-ci builds to prevent dynamic build configuration values slowing down dev machine builds.
        // See https://developer.android.com/studio/build/optimize-your-build#use_static_build_properties
        if (shouldOverrideApkAndAabNames()) {
            apkVariantOutput.outputFileName = createApkFilename(variant.name)
        }
        // Don't change version name for prod release builds or local prod release builds
        // Only change for non-prod release builds on CI to keep the release versionName free from dev values AND to prevent dynamic build configuration values slowing down dev machine builds.
        // See https://developer.android.com/studio/build/optimize-your-build#use_static_build_properties
        if (shouldOverrideVersionName(variant.name)) {
            apkVariantOutput.versionNameOverride = createComplexVersionName()
        }

        println("[applicationVariants ${variant.name}] versionNameOverride: ${apkVariantOutput.versionNameOverride}")
        println("[applicationVariants ${variant.name}] output file name: ${apkVariantOutput.outputFileName}")
    }

    /**
     * Generates a string to help identify the build between dev/QA, intended to be shown in DevOptions UI. Empty string when given [variantName] matches [BuildInfoInput.productionReleaseVariantName]
     *
     * #### Examples
     * * CI debug:    "debug-feature__update-version-name-and-apk-name-build-350-3d7f6b4-2020-05-14"
     * * local debug: "debug-feature__update-version-name-and-apk-name-dev_build-3d7f6b4-2020-05-14"
     * * release:     ""
     */
    private fun createBuildFingerprint(variantName: String): String {
        // Don't want to create the value for productionRelease (no reason for it to be present in the build)
        return if (isProductionReleaseVariant(variantName)) {
            ""
        } else {
            "$variantName-${gitBranchBuildNumberGitShaDateString()}"
        }
    }

    // TODO: TEMPLATE - Update CI debug and CI release brand name prefix values to match the updated BuildInfoInput.brandName value to show brand specific apk naming examples
    /**
     * Generates an apk filename with [BuildInfoInput.brandName], variant, build number, git sha, and date.
     *
     * **Intended to only be called for CI builds**. Using a dynamic value for apk name on dev builds slows down build time.
     * See https://developer.android.com/studio/build/optimize-your-build#use_static_build_properties
     *
     * #### Examples
     * * CI debug:      BR_Architecture-debug-feature__update-version-name-and-apk-name-build-350-3d7f6b4-2020-05-14.apk
     * * CI release:    BR_Architecture-release-feature__update-version-name-and-apk-name-build-350-3d7f6b4-2020-05-14.apk
     * * local debug:   app-debug.apk
     * * local release: app-release.apk
     */
    private fun createApkFilename(variantName: String): String {
        return createAppArtifactFilename(variantName, AppArtifact.Apk)
    }

    // TODO: TEMPLATE - Update CI debug and CI release brand name prefix values to match the updated BuildInfoInput.brandName value to show brand specific apk naming examples
    /**
     * Generates an aab filename with [BuildInfoInput.brandName], variant, build number, git sha, and date.
     *
     * **Intended to only be called for CI builds**. Using a dynamic value for aab name on dev builds slows down build time.
     * See https://developer.android.com/studio/build/optimize-your-build#use_static_build_properties
     *
     * #### Examples
     * * CI debug:      BR_Architecture-debug-feature__update-version-name-and-apk-name-build-350-3d7f6b4-2020-05-14.aab
     * * CI release:    BR_Architecture-release-feature__update-version-name-and-apk-name-build-350-3d7f6b4-2020-05-14.aab
     * * local debug:   app-debug.aab
     * * local release: app-release.aab
     */
    fun createAabFilename(variantName: String): String {
        return createAppArtifactFilename(variantName, AppArtifact.Aab)
    }

    private fun createAppArtifactFilename(variantName: String, appArtifact: AppArtifact): String {
        return "${input.brandName}-$variantName-${gitBranchBuildNumberGitShaDateString()}${appArtifact.fileExtension}"
    }

    /**
     * Generates a versionName filename starting with [AppVersion.versionName] prefix and then adding extra data as a suffix.
     *
     * **Intended to only be called for non-production release CI builds**. Using a dynamic value for apk name on dev builds slows down build time.
     * See https://developer.android.com/studio/build/optimize-your-build#use_static_build_properties
     *
     * #### Examples
     * * all CI non-prod variants (debug): 1.0.0-feature__update-version-name-and-apk-name-build-350-3d7f6b4-2020-05-14
     * * CI prod variant + local:          1.0.0
     */
    private fun createComplexVersionName(): String {
        return "${APP_VERSION.versionName}-${gitBranchBuildNumberGitShaDateString()}"
    }

    /**
     * Returns a descriptive string that can be added to certain build values (such as filename/versionName).
     *
     * #### Examples
     * * CI:    feature__update-version-name-and-apk-name-build-350-3d7f6b4-2020-05-14
     * * local: feature__update-version-name-and-apk-name-dev-build-3d7f6b4-2020-05-14
     * */
    private fun gitBranchBuildNumberGitShaDateString(): String {
        val buildDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val formattedDateString = buildDateFormatter.format(Date())
        val shortGitSha = "git rev-parse --short HEAD".execCommandWithOutput().trim()
        // git branch name with starting/ending / removed and intermediate / converted to __ to support the branch name being added to a filename.
        val gitBranchName = "git rev-parse --abbrev-ref HEAD".execCommandWithOutput().trim().removePrefix("/").removeSuffix("/").replace("/", "__")
        val resolvedBuildNumber = if (IS_CI) {
            "build-$BUILD_NUMBER"
        } else {
            "dev_build"
        }
        return "$gitBranchName-$resolvedBuildNumber-$shortGitSha-$formattedDateString"
    }

    /** True is environment variable `IS_CI` is true or `BUILD_NUMBER` is not empty (in case IS_CI is not set explicitly in the CI build configuration) */
    private fun determineIfCi(): Boolean {
        val isCiFromEnvironment = System.getenv("IS_CI")?.toBoolean() == true
        val isCiDerivedFromBuildNumber = BUILD_NUMBER.isNotEmpty()
        return isCiFromEnvironment || isCiDerivedFromBuildNumber
    }

    private fun isProductionReleaseVariant(variantName: String): Boolean = variantName == input.productionReleaseVariantName

    private fun logBuildInfo() {
        println("[logBuildInfo] APP_VERSION: ${APP_VERSION.logString()}")
        println("[logBuildInfo] BUILD_NUMBER: '$BUILD_NUMBER'")
        println("[logBuildInfo] IS_CI: $IS_CI")
    }

    /**
     * Executes a command line statement and returns the value.
     * Found at https://github.com/gradle-guides/gradle-site-plugin/blob/master/build.gradle.kts
     */
    private fun String.execCommandWithOutput(): String {
        return try {
            val parts = this.split("\\s".toRegex())
            val process: Process = ProcessBuilder(*parts.toTypedArray())
                .directory(input.rootProjectDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()
            process.waitFor(20, TimeUnit.SECONDS)
            process.inputStream.bufferedReader().readText()
        } catch (e: IOException) {
            "<empty>"
        }
    }

    /** Post build app artifact generated from a successful build. */
    private enum class AppArtifact(val fileExtension: String) {
        Apk(".apk"),
        Aab(".aab")
    }
}
