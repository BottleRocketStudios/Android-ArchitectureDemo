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

    @Suppress("MemberVisibilityCanBePrivate")
    /** Version of the app (version name and code derived from this value). */
    val APP_VERSION: AppVersion by lazy { input.appVersion }

    /** Build number set as an environment variable on the build server (or empty string for local builds) */
    private val BUILD_NUMBER: String = System.getenv("BUILD_NUMBER").orEmpty()

    /** True if this build is running an a continuous integration server (ie, Jenkins). False if running on a local dev machine. */
    private val IS_CI: Boolean = determineIfCi()

    /** True if apk name should be overridden. Otherwise false */
    fun shouldOverrideApkName(): Boolean = IS_CI
    /** True if version name should be overridden. Otherwise false */
    fun shouldOverrideVersionName(variantName: String): Boolean = IS_CI && !isProductionReleaseVariant(variantName)

    @Suppress("MemberVisibilityCanBePrivate")
    /** Call prior to android block definition in app build gradle. */
    fun initialize(buildInfoInput: BuildInfoInput) {
        if (!::input.isInitialized) {
            this.input = buildInfoInput
            logBuildInfo()
        }
    }

    /**
     * Generates a string to help identify the build between dev/QA, intended to be shown in DevOptions UI. Empty string when given [variantName] matches [BuildInfoInput.productionReleaseVariantName]
     *
     * #### Examples
     * * CI debug:    "debug-feature__update-version-name-and-apk-name-build-350-3d7f6b4-2020-05-14"
     * * local debug: "debug-feature__update-version-name-and-apk-name-dev_build-3d7f6b4-2020-05-14"
     * * release:     ""
     */
    fun createBuildFingerprint(variantName: String): String {
        // Don't want to create the value for productionRelease (no reason for it to be present in the build)
        return if (isProductionReleaseVariant(variantName)) {
            ""
        } else {
            "$variantName-${gitBranchBuildNumberGitShaDateString()}"
        }
    }

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
    fun createApkFilename(variantName: String): String {
        return "${input.brandName}-$variantName-${gitBranchBuildNumberGitShaDateString()}.apk"
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
    fun createComplexVersionName(): String {
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
}
