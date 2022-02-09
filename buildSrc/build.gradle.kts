/**
 * Helpful build script resources:
 * 1. [Use buildSrc to abstract imperative logic](https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources)
 * 2. [Info on the `kotlin-dsl` plugin](https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:kotlin-dsl_plugin)
 */
private val fileKDoc = Unit

repositories {
    google() // Needed for android gradle plugin
    mavenCentral() // The org.jetbrains.kotlin.jvm plugin requires a repository where to download the Kotlin compiler dependencies from.
}

plugins {
    `kotlin-dsl`
}

dependencies {
    // This allows usage of kotlin and android gradle plugin apis as well as importing relevant types from the plugins in buildSrc files.
    // Found in https://quickbirdstudios.com/blog/gradle-kotlin-buildsrc-plugin-android/
    // Unable to use a single source of truth due to issue mentioned in https://github.com/gradle/kotlin-dsl-samples/issues/1320#issuecomment-486309410
    // TODO: These should be updated in tandem with source of truth values in Dependencies.kt.
    implementation("com.android.tools.build:gradle:7.1.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
}
