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

val APP_VERSION = AppVersion(major = 1, minor = 0, patch = 0, hotfix = 0, showEmptyPatchNumberInVersionName = true)

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
        versionCode = APP_VERSION.versionCode
        versionName = APP_VERSION.versionName
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
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isTestCoverageEnabled = true
        }
    }
}

dependencies {
    // TODO: Find a way to make sure we are aware of out-of-date versions
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
    implementation(Config.Libraries.VAULT)
    implementation(Config.Libraries.COMMONS_CODEC)

    // Test
    testImplementation(Config.TestLibraries.JUNIT)
    testImplementation(Config.TestLibraries.MOCKITO_KOTLIN)
    testImplementation(Config.TestLibraries.TRUTH)
    testImplementation(Config.TestLibraries.ARCH_CORE_TESTING)

    androidTestImplementation(Config.TestLibraries.ESPRESSO_CORE)
}
