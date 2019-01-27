import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import org.gradle.internal.impldep.com.amazonaws.PredefinedClientConfigurations.defaultConfig
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.bottlerocketstudios.brarchitecture"
        minSdkVersion(23)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

object DependencyVersions {
    const val APP_COMPAT = "1.0.2"
    const val RETROFIT = "2.4.0"
    const val MOSHI = "1.6.0"
    const val TIMBER = "4.7.1"
    const val CONST_CODEC = "20041127.091804"
    const val KOTLIN_COROUTINES = "1.1.0"
    const val JUNIT = "4.12"
    const val MOCKITO_KOTLIN = "2.1.0"
    const val TRUTH = "0.42"
    const val TEST_RUNNER = "1.0.2"
    const val ESPRESSO = "3.0.2"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("androidx.appcompat:appcompat:${DependencyVersions.APP_COMPAT}")
    implementation("com.squareup.retrofit2:retrofit:${DependencyVersions.RETROFIT}")
    implementation("com.squareup.moshi:moshi-kotlin:${DependencyVersions.MOSHI}")
    implementation("com.squareup.retrofit2:converter-moshi:${DependencyVersions.RETROFIT}")
    implementation("com.jakewharton.timber:timber:${DependencyVersions.TIMBER}")
    implementation("commons-codec:commons-codec:${DependencyVersions.CONST_CODEC}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${DependencyVersions.KOTLIN_COROUTINES}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${DependencyVersions.MOSHI}")
    testImplementation("junit:junit:${DependencyVersions.JUNIT}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${DependencyVersions.MOCKITO_KOTLIN}")
    testImplementation("com.google.truth:truth:${DependencyVersions.TRUTH}")
    androidTestImplementation("com.android.support.test:runner:${DependencyVersions.TEST_RUNNER}")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:${DependencyVersions.ESPRESSO}")
}