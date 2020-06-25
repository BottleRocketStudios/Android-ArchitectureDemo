plugins {
    id(Config.ApplyPlugins.ANDROID_LIBRARY)
    id(Config.ApplyPlugins.JACOCO_ANDROID)
    kotlin(Config.ApplyPlugins.Kotlin.ANDROID)
    kotlin(Config.ApplyPlugins.Kotlin.ANDROID_EXTENSIONS)
    kotlin(Config.ApplyPlugins.Kotlin.KAPT)
}

android {
    compileSdkVersion(Config.AndroidSdkVersions.COMPILE_SDK)
    buildToolsVersion = Config.AndroidSdkVersions.BUILD_TOOLS

    defaultConfig {
        minSdkVersion(Config.AndroidSdkVersions.MIN_SDK)
        targetSdkVersion(Config.AndroidSdkVersions.TARGET_SDK)
        versionCode = BuildInfoManager.APP_VERSION.versionCode
        versionName = BuildInfoManager.APP_VERSION.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            // Disabling as leaving it enabled can cause the build to hang at the jacocoDebug task for 5+ minutes with no observed adverse effects when executing
            // the jacocoTest...UnitTestReport tasks. Stopping and restarting build would allow compilation/installation to complete.
            // Disable suggestion found at https://github.com/opendatakit/collect/issues/3262#issuecomment-546815946
            isTestCoverageEnabled = false
        }
    }
}

dependencies {
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

