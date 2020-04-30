/**
 * Helpful build script resources:
 * 1. [Use buildSrc to abstract imperative logic](https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources)
 * 2. [Info on the `kotlin-dsl` plugin](https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:kotlin-dsl_plugin)
 */
private val fileKDoc = Unit

repositories {
    // The org.jetbrains.kotlin.jvm plugin requires a repository where to download the Kotlin compiler dependencies from.
    jcenter()
}

plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}