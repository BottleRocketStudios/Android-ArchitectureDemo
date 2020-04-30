// Gradle docs at https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#org.gradle.api.Project:buildscript(groovy.lang.Closure)
// See also https://docs.gradle.org/current/javadoc/org/gradle/api/initialization/dsl/ScriptHandler.html and associated links for children apis
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
// Gradle docs at https://docs.gradle.org/current/dsl/org.gradle.plugin.use.PluginDependenciesSpec.html
plugins {
    id(Config.ApplyPlugins.KT_LINT) version Config.KTLINT_GRADLE_VERSION
}

// Configuration below applies to this project file and all other modules (specified in settings.gradle.kts).
// Gradle docs at https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#org.gradle.api.Project:allprojects(groovy.lang.Closure)
allprojects {
    repositories {
        google()
        jcenter()
    }
}

// Configuration below applies to all other modules (specified in settings.gradle.kts)
// Gradle docs at https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#org.gradle.api.Project:subprojects(groovy.lang.Closure)
subprojects {
    // Cannot use plugins {} here so using apply (compilation error)
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
