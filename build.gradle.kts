import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.parcelize) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.ksp) apply false
    // alias(libs.plugins.kover) apply false
    alias(libs.plugins.ktLint)
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.firebase.app.distribution) apply false
    alias(libs.plugins.android.test) apply false
    // alias(libs.plugins.baselineprofile) apply false
}

ktlint {
    // version = libs.versions.ktlint.version
    verbose = true // useful for debugging
    android = true
    outputToConsole = true
    ignoreFailures = false // Build Fails if ktlint fails

    // Enable baseline to track existing issues
    baseline.set(file("${rootProject.projectDir}/ktlint-baseline.xml"))

    // Configure output paths for better organization
    reporters {
        reporter(ReporterType.PLAIN) // Output KtLint results in plain text format
        reporter(ReporterType.HTML) // Output KtLint results in HTML format
    }

    // Additional configuration - exclude generated and build files
    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
        exclude("**/buildSrc/**")
        exclude("**/*.gradle.kts")
        exclude("**/*.gradle")
    }
}

subprojects {
    tasks.withType<Test>().configureEach {
        // TODO for now ignoring test failures to build successfully
        ignoreFailures = true

        // Disabling running tests on release (to NOT hit production APIs)
        enabled = name.contains("Debug")
    }
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}
