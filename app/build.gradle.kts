
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

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

android {
    compileSdkVersion(28)
    buildToolsVersion = "28.0.3"
    dataBinding.isEnabled = true
    defaultConfig {
        applicationId = "com.bottlerocketstudios.brarchitecture"
        minSdkVersion(23)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    // TODO: Replace with commented line when buildSrc migration complete to use a singular version of kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.0")
    // implementation(Config.Libraries.KOTLIN_REFLECT)

    // AndroidX
    implementation(Config.Libraries.APP_COMPAT)
    implementation(Config.Libraries.MATERIAL)
    implementation(Config.Libraries.LIFECYCLE_EXTENSIONS)

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
