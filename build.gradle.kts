buildscript {
    repositories {
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/") // needed for hiya jacoco
    }
    dependencies {
        classpath("com.android.tools.build", "gradle", "3.6.3")
        // classpath("com.dicedmelon.gradle", "jacoco-android", "0.1.4")
        // As the dicedmelon plugin doesn't support gradle 6 yet, using the hiya ported plugin. See https://github.com/arturdm/jacoco-android-gradle-plugin/pull/75#issuecomment-565222643
        classpath("com.hiya:jacoco-android:0.2")
        classpath(kotlin("gradle-plugin", version = "1.3.72"))
        // Gradle version plugin; use dependencyUpdates task to view third party dependency updates via `./gradlew dependencyUpdates` or AS Gradle -> android-sunseeker -> Tasks -> help -> dependencyUpdates
        // https://github.com/ben-manes/gradle-versions-plugin/releases
        classpath("com.github.ben-manes:gradle-versions-plugin:0.28.0")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

// Gradle kotlinscript syntax: https://github.com/JLLeitschuh/ktlint-gradle#idea-plugin-simple-setup
plugins {
    // https://github.com/JLLeitschuh/ktlint-gradle/blob/master/CHANGELOG.md
    // https://github.com/JLLeitschuh/ktlint-gradle/releases
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "com.github.ben-manes.versions")

    // See README.md for more info on ktlint as well as https://github.com/JLLeitschuh/ktlint-gradle#configuration
    ktlint {
        // https://github.com/pinterest/ktlint/blob/master/CHANGELOG.md
        // https://github.com/pinterest/ktlint/releases
        version.set("0.36.0")
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
            isNonStable(candidate.version) && !isNonStable(currentVersion)
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// Gradle versions plugin configuration: https://github.com/ben-manes/gradle-versions-plugin#revisions
fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
