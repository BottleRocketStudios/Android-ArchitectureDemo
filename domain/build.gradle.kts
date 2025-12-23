plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    // alias(libs.plugins.androidLibrary)
    // alias(libs.plugins.kotlinSerialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

// android {
//     namespace = libs.versions.domain.namespace.get()
//     compileSdk = libs.versions.android.compileSdk.get().toInt()
// }

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
}
