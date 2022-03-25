// Gradle docs at https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#org.gradle.api.Project:buildscript(groovy.lang.Closure)
// See also https://docs.gradle.org/current/javadoc/org/gradle/api/initialization/dsl/ScriptHandler.html and associated links for children apis
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Config.BuildScriptPlugins.ANDROID_GRADLE)
        classpath(Config.BuildScriptPlugins.KOTLIN_GRADLE)
        classpath(Config.BuildScriptPlugins.GRADLE_VERSIONS)
        classpath(Config.BuildScriptPlugins.NAVIGATION_SAFE_ARGS_GRADLE)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

// Gradle kotlinscript syntax: https://github.com/JLLeitschuh/ktlint-gradle#idea-plugin-simple-setup
// Gradle docs at https://docs.gradle.org/current/dsl/org.gradle.plugin.use.PluginDependenciesSpec.html
plugins {
    id(Config.ApplyPlugins.KT_LINT) version Config.KTLINT_GRADLE_VERSION
    id(Config.ApplyPlugins.DETEKT) version Config.DETEKT_VERSION
    id(Config.ApplyPlugins.KSP) version Config.KSP_VERSION
}

// Configuration below applies to this project file and all other modules (specified in settings.gradle.kts).
// Gradle docs at https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#org.gradle.api.Project:allprojects(groovy.lang.Closure)
allprojects {
    repositories {
        // Uncomment mavenLocal to use local versions of libraries.
        // This is used for library development of LaunchPad
        // mavenLocal()
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

// Configuration below applies to all other modules (specified in settings.gradle.kts)
// Gradle docs at https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#org.gradle.api.Project:subprojects(groovy.lang.Closure)
subprojects {
    // Cannot use plugins {} here so using apply (compilation error)
    apply(plugin = Config.ApplyPlugins.KT_LINT)
    apply(plugin = Config.ApplyPlugins.GRADLE_VERSIONS)
    apply(plugin = Config.ApplyPlugins.DETEKT)

    // See README.md for more info on ktlint as well as https://github.com/JLLeitschuh/ktlint-gradle#configuration
    ktlint {
        version.set(Config.KTLINT_VERSION)
        // debug.set(true) // useful for debugging
        verbose.set(true) // useful for debugging
        android.set(true)
        outputToConsole.set(true)
        ignoreFailures.set(false)
    }

    detekt {
        config = files("$rootDir/config/detekt/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        reports {
            html.required.set(true) // observe findings in your browser with structure and code snippets
            xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
            txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
        }
    }

    // Gradle versions plugin configuration: https://github.com/ben-manes/gradle-versions-plugin#revisions
    tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
        // Only show stable version updates for a dependency unless the dependency itself is on a non-stable version. Comment out lines below locally to see all stable/non-stable dependency updates
        rejectVersionIf {
            Config.isNonStable(candidate.version) && !Config.isNonStable(currentVersion)
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            // Enable experimental coroutines APIs. Use `-opt-in` instead of `-Xuse-experimental` or `-Xopt-in`
            // https://github.com/Kotlin/kotlinx.coroutines/issues/976#issuecomment-657102010 and https://kotlinlang.org/docs/opt-in-requirements.html#module-wide-opt-in
            @Suppress("SuspiciousCollectionReassignment")
            freeCompilerArgs += listOf(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlin.time.ExperimentalTime",
            )
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

apply(from = "jacocoConfig.gradle")
apply(from = "jacocoRoot.gradle")
