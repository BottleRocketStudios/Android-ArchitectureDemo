import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.dsl.DependencyHandler
import java.util.Locale

// Provides dependencies that can be used throughout the project build.gradle files

// https://github.com/JetBrains/kotlin/blob/master/ChangeLog.md
// https://github.com/JetBrains/kotlin/releases/latest
// https://plugins.jetbrains.com/plugin/6954-kotlin
// https://kotlinlang.org/docs/reference/whatsnew15.html
// https://kotlinlang.org/docs/releases.html#release-details
// TODO: Update corresponding buildSrc/build.gradle.kts value when updating this version!
private const val KOTLIN_VERSION = "1.6.10"
private const val KOTLIN_COROUTINES_VERSION = "1.6.0"
private const val NAVIGATION_VERSION = "2.3.5"
private const val FRAGMENT_VERSION = "1.4.1"

/**
 * Provides the source of truth for version/configuration information to any gradle build file (project and app module build.gradle.kts)
 */
object Config {
    // https://github.com/JLLeitschuh/ktlint-gradle/blob/master/CHANGELOG.md
    // https://github.com/JLLeitschuh/ktlint-gradle/releases
    const val KTLINT_GRADLE_VERSION = "10.2.1"

    // https://github.com/pinterest/ktlint/blob/master/CHANGELOG.md
    // https://github.com/pinterest/ktlint/releases
    const val KTLINT_VERSION = "0.43.2"

    // View how to execute the coverage and verifcations gradle tasks as well as how to view coverage reports in the local jacocoSetup.gradle file
    // http://www.jacoco.org/jacoco/trunk/doc/
    // https://github.com/jacoco/jacoco/releases
    // const val JACOCO_VERSION = "0.8.7" - Helper jacoco gradle files manage the jacoco plugin version due to issues reading this value inside groovy gradle files

    // Website info: https://detekt.github.io/detekt/index.html
    // Rules:
    //   https://detekt.github.io/detekt/comments.html
    //   https://detekt.github.io/detekt/complexity.html
    //   https://detekt.github.io/detekt/coroutines.html
    //   https://detekt.github.io/detekt/empty-blocks.html
    //   https://detekt.github.io/detekt/exceptions.html
    //   https://detekt.github.io/detekt/formatting.html
    //   https://detekt.github.io/detekt/naming.html
    //   https://detekt.github.io/detekt/performance.html
    //   https://detekt.github.io/detekt/style.html
    // Release info: https://github.com/detekt/detekt/releases
    const val DETEKT_VERSION = "1.19.0"
    // https://android-developers.googleblog.com/2021/09/accelerated-kotlin-build-times-with.html
    // https://github.com/google/ksp/blob/main/docs/quickstart.md
    // https://github.com/google/ksp/releases
    // TODO: First part of version should match version of kotlin - see https://github.com/google/ksp/blob/main/docs/faq.md
    const val KSP_VERSION = "1.6.10-1.0.2"

    /**
     * Called from root project buildscript block in the project root build.gradle.kts
     */
    object BuildScriptPlugins {
        // https://developer.android.com/studio/releases/gradle-plugin
        // TODO: Update corresponding buildSrc/build.gradle.kts value when updating this version!
        const val ANDROID_GRADLE = "com.android.tools.build:gradle:7.1.1"
        const val KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:$KOTLIN_VERSION"

        // Gradle version plugin; use dependencyUpdates task to view third party dependency updates via `./gradlew dependencyUpdates` or AS Gradle -> [project]] -> Tasks -> help -> dependencyUpdates
        // https://github.com/ben-manes/gradle-versions-plugin/releases
        const val GRADLE_VERSIONS = "com.github.ben-manes:gradle-versions-plugin:0.41.0"

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
        const val DETEKT = "io.gitlab.arturbosch.detekt"
        // const val JACOCO = "jacoco" // https://docs.gradle.org/current/userguide/jacoco_plugin.html - Helper jacoco gradle files manage applying the jacoco plugin
        const val NAVIGATION_SAFE_ARGS_KOTLIN = "androidx.navigation.safeargs" // Note: not using safeargs.kotlin to prevent compile time failure: https://stackoverflow.com/a/68605639/201939
        const val PARCELIZE = "kotlin-parcelize"
        const val KSP = "com.google.devtools.ksp"
        object Kotlin {
            const val ANDROID = "android"
            // Android DataBinding still requires kapt and will not migrate to use ksp: https://twitter.com/yigitboyar/status/1447408905240264704
            const val KAPT = "kapt"
        }
    }

    // What each version represents - https://medium.com/androiddevelopers/picking-your-compilesdkversion-minsdkversion-targetsdkversion-a098a0341ebd
    object AndroidSdkVersions {
        const val COMPILE_SDK = 31

        // https://developer.android.com/studio/releases/build-tools
        const val BUILD_TOOLS = "31.0.0"
        const val MIN_SDK = 23 // TODO: TEMPLATE - Replace with appropriate project minSdkVersion

        // https://developer.android.com/about/versions/12/behavior-changes-12
        const val TARGET_SDK = 31
    }

    object Compose {
        const val COMPOSE_VERSION = "1.1.0"
        const val COMPOSE_COMPILER_VERSION = "1.1.0"
    }

    // Gradle versions plugin configuration: https://github.com/ben-manes/gradle-versions-plugin#revisions
    fun isNonStable(version: String): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase(Locale.ENGLISH).contains(it) }
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
    const val CORE_KTX = "androidx.core:core-ktx:1.7.0"

    // Lifecycle
    // https://developer.android.com/jetpack/androidx/releases/lifecycle
    private const val LIFECYCLE_VERSION = "2.4.0"
    const val LIFECYCLE_LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:$LIFECYCLE_VERSION"
    const val LIFECYCLE_VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFECYCLE_VERSION"
    const val LIFECYCLE_COMMON_JAVA8 = "androidx.lifecycle:lifecycle-common-java8:$LIFECYCLE_VERSION"
    const val LIFECYCLE_COMPOSE = "androidx.lifecycle:lifecycle-viewmodel-compose:$LIFECYCLE_VERSION"

    // https://developer.android.com/jetpack/androidx/releases/appcompat
    const val APP_COMPAT = "androidx.appcompat:appcompat:1.4.1"
    // https://developer.android.com/jetpack/androidx/releases/startup
    const val STARTUP = "androidx.startup:startup-runtime:1.1.0"

    private const val ACTIVITY_VERSION = "1.4.0"
    // https://developer.android.com/jetpack/androidx/releases/activity
    const val ACTIVITY_KTX = "androidx.activity:activity-ktx:$ACTIVITY_VERSION"
    const val COMPOSE_ACTIVITY = "androidx.activity:activity-compose:$ACTIVITY_VERSION"
    // https://developer.android.com/jetpack/androidx/releases/fragment
    const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:$FRAGMENT_VERSION"

    // Compose
    // Compose-Kotlin version compatibility matrix: https://developer.android.com/jetpack/androidx/releases/compose-kotlin
    // https://developer.android.com/jetpack/androidx/releases/compose
    private const val COMPOSE_VERSION = Config.Compose.COMPOSE_VERSION
    private const val COMPOSE_COMPILER_VERSION = Config.Compose.COMPOSE_COMPILER_VERSION
    const val COMPOSE_COMPILER = "androidx.compose.compiler:compiler:$COMPOSE_COMPILER_VERSION"
    const val COMPOSE_UI = "androidx.compose.ui:ui:$COMPOSE_VERSION"
    // Tooling support (Previews, etc.)
    const val COMPOSE_UI_TOOLING = "androidx.compose.ui:ui-tooling:$COMPOSE_VERSION"
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    const val COMPOSE_FOUNDATION = "androidx.compose.foundation:foundation:$COMPOSE_VERSION"
    const val COMPOSE_ANIMATION = "androidx.compose.animation:animation:$COMPOSE_VERSION"
    // Material Design
    const val COMPOSE_MATERIAL = "androidx.compose.material:material:$COMPOSE_VERSION"
    // Material design icons
    const val COMPOSE_MATERIAL_ICONS_CORE = "androidx.compose.material:material-icons-core:$COMPOSE_VERSION"
    const val COMPOSE_MATERIAL_ICONS_EXTENDED = "androidx.compose.material:material-icons-extended:$COMPOSE_VERSION"
    // Integration with observables
    const val COMPOSE_LIVE_DATA = "androidx.compose.runtime:runtime-livedata:$COMPOSE_VERSION"

    // UI Tests
    const val COMPOSE_UI_TEST = "androidx.compose.ui:ui-test-junit4:$COMPOSE_VERSION"

    // https://developer.android.com/jetpack/androidx/releases/constraintlayout
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:2.1.3"

    // Navigation
    // https://developer.android.com/jetpack/androidx/releases/navigation
    const val NAVIGATION_FRAGMENT_KTX = "androidx.navigation:navigation-fragment-ktx:$NAVIGATION_VERSION"
    const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:$NAVIGATION_VERSION"
    const val NAVIGATION_COMPOSE = "androidx.navigation:navigation-compose:2.4.0-rc01"

    // https://security.googleblog.com/2020/02/data-encryption-on-android-with-jetpack.html
    // https://developer.android.com/topic/security/data
    // https://developer.android.com/jetpack/androidx/releases/security
    const val SECURITY_CRYPTO = "androidx.security:security-crypto:1.0.0"

    //// Material
    // https://github.com/material-components/material-components-android/releases
    const val MATERIAL = "com.google.android.material:material:1.5.0"
    // https://github.com/material-components/material-components-android-compose-theme-adapter/releases/
    // Navigate to above link, search for latest material-vX.Y.Z that supports a matching version of Compose, and just use the X.Y.Z in the dependency version below
    const val MATERIAL_COMPOSE_THEME_ADAPTER = "com.google.android.material:compose-theme-adapter:1.1.3"

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
    // https://github.com/InsertKoinIO/koin/tags
    const val KOIN_ANDROID = "io.insert-koin:koin-android:3.1.5"

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
    private const val MOSHI_VERSION = "1.13.0"
    // Note: DO NOT USE moshi-kotlin as it uses reflection via `KotlinJsonAdapterFactory`. Instead, rely on moshi and the kapt `moshi-kotlin-codegen` dependency AND annotate relevant classes with @JsonClass(generateAdapter = true)
    const val MOSHI = "com.squareup.moshi:moshi:$MOSHI_VERSION"
    const val MOSHI_KOTLIN_CODEGEN = "com.squareup.moshi:moshi-kotlin-codegen:$MOSHI_VERSION"

    //// UI
    // NOTE: groupie-databinding is deprecated. groupie-viewbinding is the replacement and should be used with both viewbinding and databinding. See https://github.com/lisawray/groupie#note
    // https://github.com/lisawray/groupie/releases
    private const val GROUPIE_VERSION = "2.10.0"
    const val GROUPIE = "com.github.lisawray.groupie:groupie:$GROUPIE_VERSION"
    const val GROUPIE_VIEWBINDING = "com.github.lisawray.groupie:groupie-viewbinding:$GROUPIE_VERSION"
    
    // Glide
    // https://github.com/bumptech/glide/releases
    private const val GLIDE_VERSION = "4.13.0"
    const val GLIDE = "com.github.bumptech.glide:glide:$GLIDE_VERSION"

    //// Utility
    // https://github.com/BottleRocketStudios/Android-CustomLintRules/releases
    const val BR_CUSTOM_ANDROID_LINT_RULES = "com.github.BottleRocketStudios:Android-CustomLintRules:1.0.0"

    // Blog: https://proandroiddev.com/livedata-with-single-events-2395dea972a8
    // https://github.com/hadilq/LiveEvent/releases
    const val LIVE_EVENT = "com.github.hadilq.liveevent:liveevent:1.2.0"

    // https://github.com/JakeWharton/timber/blob/master/CHANGELOG.md
    // https://github.com/JakeWharton/timber/releases
    const val TIMBER = "com.jakewharton.timber:timber:5.0.1"

    // https://github.com/JakeWharton/ProcessPhoenix/blob/master/CHANGELOG.md
    // https://github.com/JakeWharton/ProcessPhoenix/releases
    const val PROCESS_PHOENIX = "com.jakewharton:process-phoenix:2.1.2"

    // Commons codec - used for base64 operations (no android framework requirement)
    // https://github.com/apache/commons-codec/blob/master/RELEASE-NOTES.txt
    // https://github.com/apache/commons-codec/releases
    const val COMMONS_CODEC = "commons-codec:commons-codec:1.15"

    // https://square.github.io/leakcanary/changelog/
    // https://github.com/square/leakcanary/releases
    private const val LEAK_CANARY_VERSION = "2.8.1"
    /** Use only on debugImplementation builds */
    // Using leakcanary-android-startup since we're using the androidx startup lib: https://square.github.io/leakcanary/changelog/#androidx-app-startup
    const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android-startup:$LEAK_CANARY_VERSION"
    /** Use on all builds (via implementation) */
    const val LEAK_CANARY_PLUMBER = "com.squareup.leakcanary:plumber-android:$LEAK_CANARY_VERSION"

    // Chucker
    // https://medium.com/@cortinico/introducing-chucker-18f13a51b35d
    // https://github.com/ChuckerTeam/chucker/blob/develop/CHANGELOG.md
    // https://github.com/ChuckerTeam/chucker/releases
    private const val CHUCKER_VERSION = "3.5.2"
    const val CHUCKER = "com.github.ChuckerTeam.Chucker:library:$CHUCKER_VERSION"
    const val CHUCKER_NO_OP = "com.github.ChuckerTeam.Chucker:library-no-op:$CHUCKER_VERSION"
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
    const val MOCKITO_KOTLIN = "org.mockito.kotlin:mockito-kotlin:4.0.0"

    //// AndroidX - testing
    // https://developer.android.com/jetpack/androidx/releases/arch
    const val ARCH_CORE_TESTING = "androidx.arch.core:core-testing:2.1.0"
    const val ANDROIDX_TEST_CORE = "androidx.test:core:1.4.0"
    const val ANDROIDX_TEST_CORE_KTX = "androidx.test:core-ktx:1.4.0"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:3.4.0"
    const val JUNIT_EXT_RUNNER = "androidx.test.ext:junit:1.1.3"
    const val JUNIT_EXT_RUNNER_KTX = "androidx.test.ext:junit-ktx:1.1.3"
    const val ANDROIDX_FRAGMENT_TEST = "androidx.fragment:fragment-testing:$FRAGMENT_VERSION"

    //// Kotlinx Coroutine - Testing
    // https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/
    const val KOTLINX_COROUTINE_TESTING = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$KOTLIN_COROUTINES_VERSION"

    // Turbine - small emission testing lib for flows (hot or cold)
    // https://github.com/cashapp/turbine/blob/trunk/CHANGELOG.md
    // https://github.com/cashapp/turbine/releases
    const val TURBINE = "app.cash.turbine:turbine:0.7.0"
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
    ksp(Libraries.MOSHI_KOTLIN_CODEGEN)
}

fun DependencyHandler.composeDependencies() {
    implementation(Libraries.COMPOSE_COMPILER)
    implementation(Libraries.COMPOSE_UI)
    implementation(Libraries.COMPOSE_UI_TOOLING)
    implementation(Libraries.COMPOSE_FOUNDATION)

    implementation(Libraries.COMPOSE_ANIMATION)
    implementation(Libraries.COMPOSE_MATERIAL)
    implementation(Libraries.COMPOSE_MATERIAL_ICONS_CORE)
    implementation(Libraries.COMPOSE_MATERIAL_ICONS_EXTENDED)
    implementation(Libraries.COMPOSE_LIVE_DATA)
}
fun DependencyHandler.appCompatDependencies() {
    implementation(Libraries.APP_COMPAT)
}
fun DependencyHandler.activityDependencies() {
    implementation(Libraries.ACTIVITY_KTX)
    implementation(Libraries.COMPOSE_ACTIVITY)
}
fun DependencyHandler.fragmentDependencies() {
    implementation(Libraries.FRAGMENT_KTX)
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
    implementation(Libraries.LIFECYCLE_COMPOSE)
}
fun DependencyHandler.navigationDependencies() {
    implementation(Libraries.NAVIGATION_FRAGMENT_KTX)
    implementation(Libraries.NAVIGATION_UI_KTX)
    implementation(Libraries.NAVIGATION_COMPOSE)
}

fun DependencyHandler.materialDependencies() {
    implementation(Libraries.MATERIAL)
    implementation(Libraries.MATERIAL_COMPOSE_THEME_ADAPTER)
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
fun DependencyHandler.brCustomAndroidLintRules() {
    implementation(Libraries.BR_CUSTOM_ANDROID_LINT_RULES)
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

fun DependencyHandler.androidxCoreDependencies() {
    androidTestImplementation(TestLibraries.ANDROIDX_TEST_CORE)
    androidTestImplementation(TestLibraries.ANDROIDX_TEST_CORE_KTX)
}

fun DependencyHandler.extJunitRunnerDependencies() {
    androidTestImplementation(TestLibraries.JUNIT_EXT_RUNNER)
    androidTestImplementation(TestLibraries.JUNIT_EXT_RUNNER_KTX)
}

fun DependencyHandler.fragmentTestingDependencies() {
    androidTestImplementation(TestLibraries.ANDROIDX_FRAGMENT_TEST)
}

fun DependencyHandler.kotlinxCoroutineTestingDependencies() {
    testImplementation(TestLibraries.KOTLINX_COROUTINE_TESTING)
}

fun DependencyHandler.turbineDependencies() {
    testImplementation(TestLibraries.TURBINE)
}
