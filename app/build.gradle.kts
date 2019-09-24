import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("jacoco-android")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

jacoco {
    toolVersion = "0.8.3"
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
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

object DependencyVersions {
    const val APP_COMPAT = "1.0.2"
    const val REFLECT = "1.3.0"
    const val DESIGN = "1.0.0"
    const val LIFECYCLE = "2.0.0"
    const val CORE = "2.0.0"
    const val RETROFIT = "2.4.0"
    const val MOSHI = "1.6.0"
    const val TIMBER = "4.7.1"
    const val CONST_CODEC = "20041127.091804"
    const val KOTLIN_COROUTINES = "1.1.0"
    const val GROUPIE = "2.3.0"
    const val JUNIT = "4.12"
    const val MOCKITO_KOTLIN = "2.1.0"
    const val TRUTH = "0.42"
    const val TEST_RUNNER = "1.0.2"
    const val ESPRESSO = "3.1.0"
    const val NAV = "2.1.0-alpha04"
    const val VAULT = "1.4.2"
}

dependencies {
    // TODO: Find a way to make sure we are aware of out-of-date versions
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlin", "kotlin-reflect", DependencyVersions.REFLECT)
    implementation("androidx.appcompat", "appcompat", DependencyVersions.APP_COMPAT)
    implementation("com.google.android.material", "material", DependencyVersions.DESIGN)
    implementation("androidx.lifecycle:lifecycle-extensions:${DependencyVersions.LIFECYCLE}")
    implementation("com.squareup.retrofit2:retrofit:${DependencyVersions.RETROFIT}")
    implementation("com.squareup.retrofit2:converter-scalars:${DependencyVersions.RETROFIT}")
    implementation("com.squareup.moshi:moshi-kotlin:${DependencyVersions.MOSHI}")
    implementation("com.squareup.retrofit2:converter-moshi:${DependencyVersions.RETROFIT}")
    implementation("com.jakewharton.timber:timber:${DependencyVersions.TIMBER}")
    implementation("commons-codec:commons-codec:${DependencyVersions.CONST_CODEC}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${DependencyVersions.KOTLIN_COROUTINES}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${DependencyVersions.KOTLIN_COROUTINES}")
    implementation("androidx.navigation", "navigation-fragment-ktx", DependencyVersions.NAV)
    implementation("androidx.navigation", "navigation-ui", DependencyVersions.NAV)
    implementation("com.bottlerocketstudios", "vault", DependencyVersions.VAULT)
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${DependencyVersions.MOSHI}")
    implementation("com.xwray", "groupie", DependencyVersions.GROUPIE)
    implementation("com.xwray", "groupie-kotlin-android-extensions", DependencyVersions.GROUPIE)
    implementation("com.xwray", "groupie-databinding", DependencyVersions.GROUPIE)
    testImplementation("junit:junit:${DependencyVersions.JUNIT}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${DependencyVersions.MOCKITO_KOTLIN}")
    testImplementation("com.google.truth:truth:${DependencyVersions.TRUTH}")
    testImplementation("androidx.arch.core", "core-testing", DependencyVersions.CORE)
    implementation("androidx.fragment:fragment-testing:1.2.0-alpha01")
    androidTestImplementation("androidx.test.espresso:espresso-core:${DependencyVersions.ESPRESSO}")
    androidTestImplementation("androidx.test:runner:1.1.0")
    androidTestImplementation("androidx.test.ext:junit:1.0.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.1.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0")
    androidTestImplementation("androidx.test.ext:truth:1.0.0")
}