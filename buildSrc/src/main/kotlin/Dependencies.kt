import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.dsl.DependencyHandler

// Provides dependencies that can be used throughout the project build.gradle files

// https://github.com/JetBrains/kotlin/blob/master/ChangeLog.md
// https://github.com/JetBrains/kotlin/releases/latest
// https://plugins.jetbrains.com/plugin/6954-kotlin
// https://kotlinlang.org/docs/reference/whatsnew15.html
// https://kotlinlang.org/docs/releases.html#release-details
// TODO: Update corresponding buildSrc/build.gradle.kts value when updating this version!
private const val KOTLIN_VERSION = "1.5.10"
private const val KOTLIN_COROUTINES_VERSION = "1.5.0"
private const val NAVIGATION_VERSION = "2.3.5"

/**
 * Provides the source of truth for version/configuration information to any gradle build file (project and app module build.gradle.kts)
 */
object Config {
    // https://github.com/JLLeitschuh/ktlint-gradle/blob/master/CHANGELOG.md
    // https://github.com/JLLeitschuh/ktlint-gradle/releases
    const val KTLINT_GRADLE_VERSION = "10.0.0"

    // https://github.com/pinterest/ktlint/blob/master/CHANGELOG.md
    // https://github.com/pinterest/ktlint/releases
    const val KTLINT_VERSION = "0.41.0"

    // 1. Execute `./gradlew jacocoTestReport` OR `./gradlew jacocoTestInternalDebugUnitTestReport`
    // 2. Execute `open app/build/jacoco/jacocoHtml/index.html` or the `Open Jacoco Report` AS Run Configuration
    // http://www.jacoco.org/jacoco/trunk/doc/
    // https://github.com/jacoco/jacoco/releases
    const val JACOCO_VERSION = "0.8.7"

    /**
     * Called from root project buildscript block in the project root build.gradle.kts
     */
    object BuildScriptPlugins {
        // https://developer.android.com/studio/releases/gradle-plugin
        // TODO: Update corresponding buildSrc/build.gradle.kts value when updating this version!
        const val ANDROID_GRADLE = "com.android.tools.build:gradle:7.0.0"
        const val KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:$KOTLIN_VERSION"

        // Gradle version plugin; use dependencyUpdates task to view third party dependency updates via `./gradlew dependencyUpdates` or AS Gradle -> [project]] -> Tasks -> help -> dependencyUpdates
        // https://github.com/ben-manes/gradle-versions-plugin/releases
        const val GRADLE_VERSIONS = "com.github.ben-manes:gradle-versions-plugin:0.39.0"

        // https://github.com/arturdm/jacoco-android-gradle-plugin/releases
        // const val JACOCO_ANDROID = "com.dicedmelon.gradle:jacoco-android:0.1.4"
        // As the dicedmelon plugin doesn't support gradle 6 yet, using the hiya ported plugin. See https://github.com/arturdm/jacoco-android-gradle-plugin/pull/75#issuecomment-565222643
        const val JACOCO_ANDROID = "com.hiya:jacoco-android:0.2"
        const val NAVIGATION_SAFE_ARGS_GRADLE = "androidx.navigation:navigation-safe-args-gradle-plugin:$NAVIGATION_VERSION"
    }

    /**
     * Called in non-root project modules via id()[org.gradle.kotlin.dsl.PluginDependenciesSpecScope.id]
     * or kotlin()[org.gradle.kotlin.dsl.kotlin] (the PluginDependenciesSpec.kotlin extension function) in a build.gradle.kts
     */
    object ApplyPlugins {
        const val ANDROID_APPLICATION = "com.android.application"
        const val ANDROID_LIBRARY = "com.android.library"
        const val GRADLE_VERSIONS = "com.github.ben-manes.versions"
        const val KT_LINT = "org.jlleitschuh.gradle.ktlint"

        // const val JACOCO_ANDROID = "jacoco-android"
        // As the dicedmelon plugin doesn't support gradle 6 yet, using the hiya ported plugin. See https://github.com/arturdm/jacoco-android-gradle-plugin/pull/75#issuecomment-565222643
        const val JACOCO_ANDROID = "com.hiya.jacoco-android"
        const val NAVIGATION_SAFE_ARGS_KOTLIN = "androidx.navigation.safeargs.kotlin"

        const val PARCELIZE = "kotlin-parcelize"
        object Kotlin {
            const val ANDROID = "android"
            const val KAPT = "kapt"
        }
    }

    // What each version represents - https://medium.com/androiddevelopers/picking-your-compilesdkversion-minsdkversion-targetsdkversion-a098a0341ebd
    object AndroidSdkVersions {
        const val COMPILE_SDK = 30

        // https://developer.android.com/studio/releases/build-tools
        const val BUILD_TOOLS = "30.0.3"
        const val MIN_SDK = 23 // TODO: TEMPLATE - Replace with appropriate project minSdkVersion

        // https://developer.android.com/about/versions/11/behavior-changes-11
        const val TARGET_SDK = 30
    }

    // Gradle versions plugin configuration: https://github.com/ben-manes/gradle-versions-plugin#revisions
    fun isNonStable(version: String): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }
}

/**
 * Dependency Version definitions with links to source (if open source)/release notes. Defines the version in one place for multiple dependencies that use the same version.
 * Use [DependencyHandler] extension functions below that provide logical groupings of dependencies including appropriate configuration accessors.
 */
private object Libraries {
    //// AndroidX
    // All androidx dependencies version table: https://developer.android.com/jetpack/androidx/versions#version-table
    // https://developer.android.com/jetpack/androidx/releases/core
    // https://developer.android.com/kotlin/ktx#core-packages
    // https://developer.android.com/jetpack/androidx/releases/
    // https://developer.android.com/kotlin/ktx

    // https://developer.android.com/jetpack/androidx/releases/core
    const val CORE_KTX = "androidx.core:core-ktx:1.5.0"

    // Lifecycle
    // https://developer.android.com/jetpack/androidx/releases/lifecycle
    private const val LIFECYCLE_VERSION = "2.3.1"
    const val LIFECYCLE_LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:$LIFECYCLE_VERSION"
    const val LIFECYCLE_VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFECYCLE_VERSION"
    const val LIFECYCLE_COMMON_JAVA8 = "androidx.lifecycle:lifecycle-common-java8:$LIFECYCLE_VERSION"

    // https://developer.android.com/jetpack/androidx/releases/appcompat
    const val APP_COMPAT = "androidx.appcompat:appcompat:1.3.0"
    // https://developer.android.com/jetpack/androidx/releases/startup
    const val STARTUP = "androidx.startup:startup-runtime:1.0.0"

    // https://developer.android.com/jetpack/androidx/releases/constraintlayout
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:2.0.4"

    // Navigation
    // https://developer.android.com/jetpack/androidx/releases/navigation
    const val NAVIGATION_FRAGMENT_KTX = "androidx.navigation:navigation-fragment-ktx:$NAVIGATION_VERSION"
    const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:$NAVIGATION_VERSION"

    // https://security.googleblog.com/2020/02/data-encryption-on-android-with-jetpack.html
    // https://developer.android.com/topic/security/data
    // https://developer.android.com/jetpack/androidx/releases/security
    const val SECURITY_CRYPTO = "androidx.security:security-crypto:1.0.0"

    //// Material
    // https://github.com/material-components/material-components-android/releases
    const val MATERIAL = "com.google.android.material:material:1.3.0"

    //// Kotlin
    const val KOTLIN_STDLIB_JDK7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$KOTLIN_VERSION"
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:$KOTLIN_VERSION"

    //// Coroutines + Flow
    // https://kotlinlang.org/docs/reference/coroutines/flow.html
    // https://github.com/Kotlin/kotlinx.coroutines/blob/master/CHANGES.md
    // https://github.com/Kotlin/kotlinx.coroutines/releases

    const val KOTLINX_COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$KOTLIN_COROUTINES_VERSION"
    const val KOTLINX_COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$KOTLIN_COROUTINES_VERSION"

    //// Koin
    // https://github.com/InsertKoinIO/koin/blob/master/CHANGELOG.md
    // https://github.com/InsertKoinIO/koin/releases
    const val KOIN_ANDROID = "io.insert-koin:koin-android:3.0.2"

    //// Core Library Desugaring
    // https://developer.android.com/studio/write/java8-support#library-desugaring
    // https://developer.android.com/studio/write/java8-support-table
    // https://github.com/google/desugar_jdk_libs
    // https://github.com/google/desugar_jdk_libs/blob/master/VERSION.txt
    private const val DESUGAR_VERSION = "1.1.5"
    const val CORE_LIBRARY_DESUGARING = "com.android.tools:desugar_jdk_libs:$DESUGAR_VERSION"

    //// Retrofit
    // javadoc: https://square.github.io/retrofit/2.x/retrofit/
    // https://github.com/square/retrofit/blob/master/CHANGELOG.md
    // https://github.com/square/retrofit/releases
    private const val RETROFIT_VERSION = "2.9.0"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:$RETROFIT_VERSION"
    const val RETROFIT_SCALARS_CONVERTER = "com.squareup.retrofit2:converter-scalars:$RETROFIT_VERSION"
    const val RETROFIT_MOSHI_CONVERTER = "com.squareup.retrofit2:converter-moshi:$RETROFIT_VERSION"

    //// Moshi
    // https://github.com/square/moshi/blob/master/CHANGELOG.md
    // https://github.com/square/moshi/releases
    private const val MOSHI_VERSION = "1.12.0"
    // Note: DO NOT USE moshi-kotlin as it uses reflection via `KotlinJsonAdapterFactory`. Instead, rely on moshi and the kapt `moshi-kotlin-codegen` dependency AND annotate relevant classes with @JsonClass(generateAdapter = true)
    const val MOSHI = "com.squareup.moshi:moshi:$MOSHI_VERSION"
    const val MOSHI_KOTLIN_CODEGEN = "com.squareup.moshi:moshi-kotlin-codegen:$MOSHI_VERSION"

    //// UI
    // NOTE: groupie-databinding is deprecated. groupie-viewbinding is the replacement and should be used with both viewbinding and databinding. See https://github.com/lisawray/groupie#note
    // https://github.com/lisawray/groupie/releases
    private const val GROUPIE_VERSION = "2.9.0"
    const val GROUPIE = "com.github.lisawray.groupie:groupie:$GROUPIE_VERSION"
    const val GROUPIE_VIEWBINDING = "com.github.lisawray.groupie:groupie-viewbinding:$GROUPIE_VERSION"
    
    // Glide
    // https://github.com/bumptech/glide/releases
    private const val GLIDE_VERSION = "4.12.0"
    const val GLIDE = "com.github.bumptech.glide:glide:$GLIDE_VERSION"

    //// Utility
    // Blog: https://proandroiddev.com/livedata-with-single-events-2395dea972a8
    // https://github.com/hadilq/LiveEvent/releases
    const val LIVE_EVENT = "com.github.hadilq.liveevent:liveevent:1.2.0"

    // https://github.com/JakeWharton/timber/blob/master/CHANGELOG.md
    // https://github.com/JakeWharton/timber/releases
    // TODO: Remove lint { disable "TIMBER-LINT-RULES" } from app AND data build.gradle.kts once Timber is updated past 4.7.1 to fix lint: https://github.com/JakeWharton/timber/issues/408
    const val TIMBER = "com.jakewharton.timber:timber:4.7.1"

    // https://github.com/JakeWharton/ProcessPhoenix/blob/master/CHANGELOG.md
    // https://github.com/JakeWharton/ProcessPhoenix/releases
    const val PROCESS_PHOENIX = "com.jakewharton:process-phoenix:2.0.0"

    // Commons codec - used for base64 operations (no android framework requirement)
    // https://github.com/apache/commons-codec/blob/master/RELEASE-NOTES.txt
    // https://github.com/apache/commons-codec/releases
    const val COMMONS_CODEC = "commons-codec:commons-codec:1.15"

    // https://square.github.io/leakcanary/changelog/
    // https://github.com/square/leakcanary/releases
    private const val LEAK_CANARY_VERSION = "2.7"
    /** Use only on debugImplementation builds */
    const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:$LEAK_CANARY_VERSION"
    /** Use on all builds (via implementation) */
    const val LEAK_CANARY_PLUMBER = "com.squareup.leakcanary:plumber-android:$LEAK_CANARY_VERSION"

    // Chucker
    // https://medium.com/@cortinico/introducing-chucker-18f13a51b35d
    // https://github.com/ChuckerTeam/chucker/blob/develop/CHANGELOG.md
    // https://github.com/ChuckerTeam/chucker/releases
    private const val CHUCKER_VERSION = "3.4.0"
    const val CHUCKER = "com.github.ChuckerTeam.Chucker:library:$CHUCKER_VERSION"
    const val CHUCKER_NO_OP = "com.github.ChuckerTeam.Chucker:library-no-op:$CHUCKER_VERSION"

    // https://github.com/amitshekhariitbhu/Android-Debug-Database/blob/master/CHANGELOG.md
    // https://github.com/amitshekhariitbhu/Android-Debug-Database/releases
    // Using jitpack: https://github.com/amitshekhariitbhu/Android-Debug-Database/issues/208
    const val DEBUG_DATABASE = "com.github.amitshekhariitbhu.Android-Debug-Database:debug-db:v1.0.6"
}

/**
 * test and/or androidTest specific dependencies.
 * Use [DependencyHandler] extension functions below that provide logical groupings of dependencies including appropriate configuration accessors.
 */
private object TestLibraries {
    // https://github.com/junit-team/junit4/releases
    // https://github.com/junit-team/junit4/blob/master/doc/ReleaseNotes4.13.md
    const val JUNIT = "junit:junit:4.13.2"

    // main site: https://google.github.io/truth/
    // comparison to other assertion libs: https://google.github.io/truth/comparison
    // benefits: https://google.github.io/truth/benefits
    // javadocs: https://truth.dev/api/1.0/
    // https://github.com/google/truth/releases
    const val TRUTH = "com.google.truth:truth:1.1.3"

    // https://github.com/mockito/mockito-kotlin/wiki/Mocking-and-verifying
    // https://github.com/mockito/mockito-kotlin/releases
    const val MOCKITO_KOTLIN = "org.mockito.kotlin:mockito-kotlin:3.2.0"

    //// AndroidX - testing
    // https://developer.android.com/jetpack/androidx/releases/arch
    const val ARCH_CORE_TESTING = "androidx.arch.core:core-testing:2.1.0"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:3.3.0"

    //// Kotlinx Coroutine - Testing
    // https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/
    const val KOTLINX_COROUTINE_TESTING = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$KOTLIN_COROUTINES_VERSION"
}

//// Dependency Groups - to be used inside dependencies {} block instead of declaring all necessary lines for a particular dependency
//// See DependencyHandlerUtils.kt to define DependencyHandler extension functions for types not handled (ex: compileOnly).
//// More info in BEST_PRACTICES.md -> Build section
fun DependencyHandler.kotlinDependencies() {
    implementation(Libraries.KOTLIN_STDLIB_JDK7)
    implementation(Libraries.KOTLIN_REFLECT)
}

fun DependencyHandler.coroutineDependencies() {
    implementation(Libraries.KOTLINX_COROUTINES_CORE)
    implementation(Libraries.KOTLINX_COROUTINES_ANDROID)
}

fun DependencyHandler.koinDependencies() {
    implementation(Libraries.KOIN_ANDROID)
}
fun DependencyHandler.coreLibraryDesugaringDependencies() {
    coreLibraryDesugaring(Libraries.CORE_LIBRARY_DESUGARING)
}
fun DependencyHandler.retrofitDependencies() {
    implementation(Libraries.RETROFIT)
    implementation(Libraries.RETROFIT_SCALARS_CONVERTER)
    implementation(Libraries.RETROFIT_MOSHI_CONVERTER)
}

fun DependencyHandler.moshiDependencies() {
    api(Libraries.MOSHI)
    kapt(Libraries.MOSHI_KOTLIN_CODEGEN)
}

fun DependencyHandler.appCompatDependencies() {
    implementation(Libraries.APP_COMPAT)
}
fun DependencyHandler.androidxStartupDependencies() {
    implementation(Libraries.STARTUP)
}
fun DependencyHandler.constraintLayoutDependencies() {
    implementation(Libraries.CONSTRAINT_LAYOUT)
}

fun DependencyHandler.lifecycleDependencies() {
    implementation(Libraries.LIFECYCLE_LIVEDATA_KTX)
    implementation(Libraries.LIFECYCLE_VIEWMODEL_KTX)
    implementation(Libraries.LIFECYCLE_COMMON_JAVA8)
}
fun DependencyHandler.navigationDependencies() {
    implementation(Libraries.NAVIGATION_FRAGMENT_KTX)
    implementation(Libraries.NAVIGATION_UI_KTX)
}

fun DependencyHandler.materialDependencies() {
    implementation(Libraries.MATERIAL)
}

fun DependencyHandler.coreKtxDependencies() {
    implementation(Libraries.CORE_KTX)
}

fun DependencyHandler.securityCryptoDependencies() {
    implementation(Libraries.SECURITY_CRYPTO)
}

fun DependencyHandler.groupieDependencies() {
    implementation(Libraries.GROUPIE)
    implementation(Libraries.GROUPIE_VIEWBINDING)
}

fun DependencyHandler.glideDependencies() {
    implementation(Libraries.GLIDE)
}

fun DependencyHandler.timberDependencies() {
    implementation(Libraries.TIMBER)
}
fun DependencyHandler.processPhoenixDependencies() {
    implementation(Libraries.PROCESS_PHOENIX)
}
fun DependencyHandler.liveEventDependencies() {
    implementation(Libraries.LIVE_EVENT)
}

fun DependencyHandler.commonsCodecDependencies() {
    implementation(Libraries.COMMONS_CODEC)
}

fun DependencyHandler.leakCanaryDependencies() {
    debugImplementation(Libraries.LEAK_CANARY) // note the debugImplementation usage (no releaseImplementation) - intentionally bypass adding to internalRelease build unless QA has a reason to need it
    implementation(Libraries.LEAK_CANARY_PLUMBER) // note that plumber is expected to be used for all build types (debug and release): https://square.github.io/leakcanary/changelog/#more-leak-fixes-in-plumber
}

fun DependencyHandler.chuckerDependencies(devConfigurations: List<Configuration>, productionConfiguration: Configuration) {
    // Only add dependency for dev configurations in the list
    devConfigurations.forEach { devConfiguration: Configuration ->
        add(devConfiguration.name, Libraries.CHUCKER)
    }
    // Production configuration is a no-op
    add(productionConfiguration.name, Libraries.CHUCKER_NO_OP) // note the releaseImplementation no-op
}

fun DependencyHandler.debugDatabaseDependencies(devConfigurations: List<Configuration>) {
    // Only add dependency for dev configurations in the list
    devConfigurations.forEach { devConfiguration: Configuration ->
        add(devConfiguration.name, Libraries.DEBUG_DATABASE)
    }
}

// Test specific dependency groups
fun DependencyHandler.junitDependencies() {
    testImplementation(TestLibraries.JUNIT)
}

fun DependencyHandler.mockitoKotlinDependencies() {
    testImplementation(TestLibraries.MOCKITO_KOTLIN)
}

fun DependencyHandler.truthDependencies() {
    testImplementation(TestLibraries.TRUTH)
}

fun DependencyHandler.archCoreTestingDependencies() {
    testImplementation(TestLibraries.ARCH_CORE_TESTING)
}

fun DependencyHandler.espressoDependencies() {
    androidTestImplementation(TestLibraries.ESPRESSO_CORE)
}

fun DependencyHandler.kotlinxCoroutineTestingDependencies() {
    testImplementation(TestLibraries.KOTLINX_COROUTINE_TESTING)
}
