buildscript {
    repositories {
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/") // needed for hiya jacoco
    }
    dependencies {
        classpath(Config.BuildScriptPlugins.ANDROID_GRADLE)
        classpath(Config.BuildScriptPlugins.KOTLIN_GRADLE)
        classpath(Config.BuildScriptPlugins.GRADLE_VERSIONS)
        classpath(Config.BuildScriptPlugins.JACOCO_ANDROID)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

// Gradle kotlinscript syntax: https://github.com/JLLeitschuh/ktlint-gradle#idea-plugin-simple-setup
plugins {
    id(Config.ApplyPlugins.KT_LINT) version Config.KTLINT_GRADLE_VERSION
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

subprojects {
    apply(plugin = Config.ApplyPlugins.KT_LINT)
    apply(plugin = Config.ApplyPlugins.GRADLE_VERSIONS)

    // See README.md for more info on ktlint as well as https://github.com/JLLeitschuh/ktlint-gradle#configuration
    ktlint {
        version.set(Config.KTLINT_VERSION)
        // debug.set(true) // useful for debugging
        verbose.set(true) // useful for debugging
        android.set(true)
        outputToConsole.set(true)
        ignoreFailures.set(false)
    }

    // Gradle versions plugin configuration: https://github.com/ben-manes/gradle-versions-plugin#revisions
    tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
        // Only show stable version updates for a dependency unless the dependency itself is on a non-stable version. Comment out lines below locally to see all stable/non-stable dependency updates
        rejectVersionIf {
            Config.isNonStable(candidate.version) && !Config.isNonStable(currentVersion)
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
