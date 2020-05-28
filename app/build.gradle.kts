import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput

plugins {
    id(Config.ApplyPlugins.ANDROID_APPLICATION)
    id(Config.ApplyPlugins.JACOCO_ANDROID)
    kotlin(Config.ApplyPlugins.Kotlin.ANDROID)
    kotlin(Config.ApplyPlugins.Kotlin.ANDROID_EXTENSIONS)
    kotlin(Config.ApplyPlugins.Kotlin.KAPT)
    id(Config.ApplyPlugins.NAVIGATION_SAFE_ARGS_KOTLIN)
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
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
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
        BuildInfoManager.createBuildIdentifier(variant)
        variant.outputs.all {
            val baseVariantOutput: BaseVariantOutput = this
            BuildInfoManager.modifyVersionNameAndApkName(variant, baseVariantOutput)
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    // TODO: Find a way to make sure we are aware of out-of-date versions of any static aars/jars in /libs. Manually check for any updates at/prior to dev signoff.
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin/coroutines
    kotlinDependencies()
    coroutineDependencies()

    // AndroidX
    appCompatDependencies()
    materialDependencies()
    lifecycleDependencies()
    navigationDependencies()
    securityCryptoDependencies()

    koinDependencies()

    // Networking/parsing
    retrofitDependencies()
    moshiDependencies()

    // UI
    groupieDependencies()

    // Utility
    liveEventDependencies()
    timberDependencies()
    commonsCodecDependencies()
    leakCanaryDependencies()
    chuckerDependencies()
    debugDatabaseDependencies()

    // Test
    junitDependencies()
    mockitoKotlinDependencies()
    truthDependencies()
    archCoreTestingDependencies()
    // Android Test
    espressoDependencies()
}
