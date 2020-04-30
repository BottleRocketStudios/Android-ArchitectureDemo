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
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
