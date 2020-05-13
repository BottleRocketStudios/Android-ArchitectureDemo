import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput

plugins {
    id(Config.ApplyPlugins.ANDROID_APPLICATION)
    id(Config.ApplyPlugins.JACOCO_ANDROID)
    kotlin(Config.ApplyPlugins.Kotlin.ANDROID)
    kotlin(Config.ApplyPlugins.Kotlin.ANDROID_EXTENSIONS)
    kotlin(Config.ApplyPlugins.Kotlin.KAPT)
}

jacoco {
    toolVersion = Config.JACOCO_VERSION
}

// Prep BuildInfoManager to use its functions/properties later throughout this build script
BuildInfoManager.initialize(
    BuildInfoInput(
        appVersion = AppVersion(major = 1, minor = 0, patch = 0, hotfix = 0, showEmptyPatchNumberInVersionName = true),
        brandName = "BR_Architecture",
        productionReleaseVariantName = "release",
        rootProjectDir = rootDir
    )
)

// Some documentation on inner tags/blocks can be found with the below urls:
// android {...} DSL Reference:
// http://google.github.io/android-gradle-dsl/ - Select version of AGP
// https://google.github.io/android-gradle-dsl/current/index.html - Latest version of AGP
// javadocs:
// http://google.github.io/android-gradle-dsl/javadoc/ - Select version of AGP
// http://google.github.io/android-gradle-dsl/javadoc/current/ - Latest version of AGP
android {
    compileSdkVersion(Config.AndroidSdkVersions.COMPILE_SDK)
    buildToolsVersion = Config.AndroidSdkVersions.BUILD_TOOLS
    dataBinding.isEnabled = true
    defaultConfig {
        applicationId = "com.bottlerocketstudios.brarchitecture"
        minSdkVersion(Config.AndroidSdkVersions.MIN_SDK)
        targetSdkVersion(Config.AndroidSdkVersions.TARGET_SDK)
        versionCode = BuildInfoManager.APP_VERSION.versionCode
        versionName = BuildInfoManager.APP_VERSION.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    // https://stackoverflow.com/questions/48988778/cannot-inline-bytecode-built-with-jvm-target-1-8-into-bytecode-that-is-being-bui#comment93879366_50991772
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    signingConfigs {
        getByName("debug") {
            // Use common debug keystore so all local builds can be shared between devs/QA
            storeFile = file("../keystore/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            // FIXME: HARDCODED TO USE DEBUG KEYSTORE!!! DO NOT SHIP THIS!!! ADD LOGIC TO USE ACTUAL RELEASE KEYSTORE VIA ENVIRONMENT VARIABLES ON CI!!!
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("debug") {
            // Disabling as leaving it enabled can cause the build to hang at the jacocoDebug task for 5+ minutes with no observed adverse effects when executing
            // the jacocoTest...UnitTestReport tasks. Stopping and restarting build would allow compilation/installation to complete.
            // Disable suggestion found at https://github.com/opendatakit/collect/issues/3262#issuecomment-546815946
            isTestCoverageEnabled = false
        }
    }
    applicationVariants.all {
        // Using a local val here since attempting to use a named lambda parameter would change the function signature from operating on applicationVariants.all (with an `Action` parameter)
        // to the Collections Iterable.`all` function. Same thing applies to outputs.all below
        val variant: ApplicationVariant = this
        createBuildIdentifier(variant)
        variant.outputs.all {
            val baseVariantOutput: BaseVariantOutput = this
            modifyVersionNameAndApkName(variant, baseVariantOutput)
        }
    }
}

dependencies {
    // TODO: Find a way to make sure we are aware of out-of-date versions of any static aars/jars in /libs. Manually check for any updates at/prior to dev signoff.
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin
    implementation(Config.Libraries.KOTLIN_STDLIB_JDK7)
    implementation(Config.Libraries.KOTLIN_REFLECT)

    // AndroidX
    implementation(Config.Libraries.APP_COMPAT)
    implementation(Config.Libraries.MATERIAL)
    implementation(Config.Libraries.LIFECYCLE_LIVEDATA_KTX)
    implementation(Config.Libraries.LIFECYCLE_VIEWMODEL_KTX)
    implementation(Config.Libraries.LIFECYCLE_COMMON_JAVA8)
    implementation(Config.Libraries.SECURITY_CRYPTO)

    // Coroutines
    implementation(Config.Libraries.KOTLINX_COROUTINES_CORE)
    implementation(Config.Libraries.KOTLINX_COROUTINES_ANDROID)

    // Retrofit
    implementation(Config.Libraries.RETROFIT)
    implementation(Config.Libraries.RETROFIT_SCALARS_CONVERTER)
    implementation(Config.Libraries.RETROFIT_MOSHI_CONVERTER)

    // Moshi
    implementation(Config.Libraries.MOSHI_KOTLIN)
    kapt(Config.Libraries.MOSHI_KOTLIN_CODEGEN)

    // UI
    implementation(Config.Libraries.GROUPIE)
    implementation(Config.Libraries.GROUPIE_KOTLIN_ANDROID_EXTENSIONS)
    implementation(Config.Libraries.GROUPIE_DATABINDING)

    // Utility
    implementation(Config.Libraries.TIMBER)
    implementation(Config.Libraries.COMMONS_CODEC)
    debugImplementation(Config.Libraries.LEAK_CANARY) // note the debugImplementation usage (no releaseImplementation)
    debugImplementation(Config.Libraries.CHUCKER) // note the debugImplementation usage (releaseImplementation uses no-op)
    releaseImplementation(Config.Libraries.CHUCKER_NO_OP) // note the releaseImplementation no-op
    debugImplementation(Config.Libraries.DEBUG_DATABASE) // note the debugImplementation usage (no releaseImplementation)

    // Test
    testImplementation(Config.TestLibraries.JUNIT)
    testImplementation(Config.TestLibraries.MOCKITO_KOTLIN)
    testImplementation(Config.TestLibraries.TRUTH)
    testImplementation(Config.TestLibraries.ARCH_CORE_TESTING)

    androidTestImplementation(Config.TestLibraries.ESPRESSO_CORE)
}

/** Creates BUILD_IDENTIFIER (accessible in code via BuildConfig.BUILD_IDENTIFIER) */
fun createBuildIdentifier(variant: ApplicationVariant) {
    println("[applicationVariants ${variant.name}] versionName: ${variant.versionName}")
    val buildFingerprint = BuildInfoManager.createBuildFingerprint(variant.name)
    // Write to BUILD_IDENTIFIER to be used within the app's code (dev screen UI)
    variant.buildConfigField("String", "BUILD_IDENTIFIER", "\"$buildFingerprint\"")
    println("[applicationVariants ${variant.name}] buildFingerprint: '$buildFingerprint'")
}

/** Updates version name and apk name when appropriate */
fun modifyVersionNameAndApkName(variant: ApplicationVariant, output: BaseVariantOutput) {
    // ApkVariantOutput provides setVersionNameOverride and setOutputFileName
    // https://android.googlesource.com/platform/tools/base/+/studio-master-dev/build-system/gradle-core/src/main/java/com/android/build/gradle/api/ApkVariantOutput.java
    // Initial finding pointing out the setVersionNameOverride usage: https://stackoverflow.com/a/47053539/201939
    val apkVariantOutput: ApkVariantOutput = output as ApkVariantOutput

    // Don't change apk name for non-ci builds to prevent dynamic build configuration values slowing down dev machine builds.
    // See https://developer.android.com/studio/build/optimize-your-build#use_static_build_properties
    if (BuildInfoManager.shouldOverrideApkName()) {
        apkVariantOutput.outputFileName = BuildInfoManager.createApkFilename(variant.name)
    }
    // Don't change version name for prod release builds or local prod release builds
    // Only change for non-prod release builds on CI to keep the release versionName free from dev values AND to prevent dynamic build configuration values slowing down dev machine builds.
    // See https://developer.android.com/studio/build/optimize-your-build#use_static_build_properties
    if (BuildInfoManager.shouldOverrideVersionName(variant.name)) {
        apkVariantOutput.versionNameOverride = BuildInfoManager.createComplexVersionName()
    }

    println("[applicationVariants ${variant.name}] versionNameOverride: ${apkVariantOutput.versionNameOverride}")
    println("[applicationVariants ${variant.name}] output file name: ${apkVariantOutput.outputFileName}")
}
