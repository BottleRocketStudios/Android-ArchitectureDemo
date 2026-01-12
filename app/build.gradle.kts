import com.google.firebase.appdistribution.gradle.firebaseAppDistribution
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import java.io.FileInputStream
import java.util.Properties
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.app.distribution)
    // alias(libs.plugins.kover)
    alias(libs.plugins.ktLint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.firebase.perf)
    // alias(libs.plugins.baselineprofile)
}

android {
    namespace = libs.versions.app.namespace.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.app.namespace.get()
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "BUILD_IDENTIFIER", "\"debug\"")
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    signingConfigs {
        getByName("debug") {
            // Common debug keystore so all local builds can be shared between devs/QA
            storeFile = file("../keystore/debug.keystore") // TODO: TEMPLATE - Generate a new debug keystore (optional). More info in NEW_PROJECT_STARTER.md
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            // Release keystore expected to be present in environment variables (living on the build server)
            storeFile = file(System.getenv("_KEYSTORE") ?: "_KEYSTORE environment variable not set for release build type; unable to compile the current variant")
            storePassword = System.getenv("_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("_KEY_ALIAS")
            keyPassword = System.getenv("_KEY_PASSWORD")
        }
    }
    // See BEST_PRACTICES.md for comments on purpose of each build type/flavor/variant
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            // Disabling as leaving it enabled can cause the build to hang at the jacocoDebug task for 5+ minutes with no observed adverse effects when executing
            // the test...UnitTestCoverage tasks. Stopping and restarting build would allow compilation/installation to complete.
            // Disable suggestion found at https://github.com/opendatakit/collect/issues/3262#issuecomment-546815946
            enableUnitTestCoverage = false
            firebaseAppDistribution {
                releaseNotes="App distribution for Arch Demo"
                testers="colin.shelton@bottlerocketstudios.com"
            }
        }
        // Create debug minified buildtype to allow attaching debugger to minified build: https://medium.com/androiddevelopers/practical-proguard-rules-examples-5640a3907dc9
        create("debugMini") {
            initWith(getByName("debug"))
            matchingFallbacks += listOf("debug")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    flavorDimensions += listOf("environment")
    // See BEST_PRACTICES.md for comments on purpose of each build type/flavor/variant
    productFlavors {
        create("internal") {
            applicationId = "com.bottlerocketstudios.brarchitecture.internal" // TODO: TEMPLATE - Replace with appropriate project applicationId prefix, leaving .internal
            versionNameSuffix = "-internal"
            buildConfigField("boolean", "INTERNAL", "true")
            buildConfigField("boolean", "PRODUCTION", "false")
            dimension = "environment"
        }
        create("production") {
            applicationId = "com.bottlerocketstudios.brarchitecture" // TODO: TEMPLATE - Replace full string with appropriate project applicationId
            buildConfigField("boolean", "INTERNAL", "false")
            buildConfigField("boolean", "PRODUCTION", "true")
            dimension = "environment"
        }
    }
}

ktlint {
    version = libs.versions.ktlint.version
    verbose = true // useful for debugging
    android = true
    outputToConsole = true
    ignoreFailures = false

    reporters {
        reporter(ReporterType.PLAIN) // Output KtLint results in plain text format
        reporter(ReporterType.HTML) // Output KtLint results in HTML format
    }

    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
    }
}

dependencies {
    implementation(project(mapOf("path" to ":domain")))

    implementation(project(mapOf("path" to ":data")))
    implementation(project(mapOf("path" to ":compose")))

    implementation(libs.chucker)

    // Google Credential Manager
    implementation(libs.google.credentials)
    implementation(libs.google.credentials.play.services.auth)

    // AndroidX
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.androidx.window)
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.activity)
    implementation(libs.compose.animation)

    implementation(libs.compose.foundation)
    implementation(libs.material)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material.ripple)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3.window.size)
    implementation(libs.compose.runtime)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)

    // Lifecycle
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.lifecycle.livedata)

    // Kotlin / Coroutines
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    // implementation(libs.play.services.coroutines)
    implementation(libs.kotlinx.serialization.json)

    // Koin
    implementation(libs.koin)
    implementation(libs.koin.compose)

    // Firebase
    implementation(project.dependencies.platform(libs.firebase.bom))
    implementation(libs.firebase.config)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.perf)

    // Firebase open source
    implementation(libs.firebase.open.source.config)

    // Utility
    implementation(libs.accompanist.permissions)
    implementation(libs.android.lint.rules)
    implementation(libs.androidx.security.crypto)
    implementation(libs.coil)
    implementation(libs.ktor)
    implementation(libs.live.event) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-android-extensions-runtime")
    }
    implementation(libs.process.phoenix)
    implementation(libs.timber)

    implementation(libs.compose.material)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Android Test
    // androidTestImplementation(libs.androidx.junit.runner)
    // androidTestImplementation(libs.androidx.junit.runner.ktx)
    // androidTestImplementation(libs.androidx.test.core)
    // androidTestImplementation(libs.androidx.test.core.ktx)
    // androidTestImplementation(libs.androidx.test.espresso)
    // androidTestImplementation(platform(libs.compose.bom))
    // androidTestImplementation(libs.androidx.ui.test.junit4)

    // test
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito)
    testImplementation(libs.mockk)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
    testImplementation(libs.koin.android.test)
    coreLibraryDesugaring(libs.core.library.desugaring)
}
