buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build", "gradle", "3.3.1")
        classpath("com.dicedmelon.gradle", "jacoco-android", "0.1.4")
        classpath(kotlin("gradle-plugin", version = "1.3.11"))
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0-alpha04")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}