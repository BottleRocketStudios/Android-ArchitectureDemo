import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.androidLibrary)

    alias(libs.plugins.ksp)
    alias(libs.plugins.parcelize)
    // alias(libs.plugins.screenshot)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ktLint)
}

android {
    namespace = libs.versions.compose.namespace.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        lint.targetSdk = libs.versions.android.targetSdk.get().toInt()
        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        // isCoreLibraryDesugaringEnabled = true
    }
    // kotlinOptions {
    //    jvmTarget = JavaVersion.VERSION_17.toString()
    // }
    buildFeatures {
        buildConfig = true
        compose = true
    }


    buildTypes {
        // Create debug minified buildtype to allow attaching debugger to minified build: https://medium.com/androiddevelopers/practical-proguard-rules-examples-5640a3907dc9
        create("debugMini") {
            initWith(getByName("debug"))
            matchingFallbacks += listOf("debug")
        }
    }
    flavorDimensions += listOf("environment")
    // See BEST_PRACTICES.md for comments on purpose of each build type/flavor/variant
    productFlavors {
        create("internal") {
            buildConfigField("boolean", "INTERNAL", "true")
            buildConfigField("boolean", "PRODUCTION", "false")
            dimension = "environment"
        }
        create("production") {
            buildConfigField("boolean", "INTERNAL", "false")
            buildConfigField("boolean", "PRODUCTION", "true")
            dimension = "environment"
        }
    }
    androidComponents {
        beforeVariants(selector().all()) { variant ->
            if (variant.name == "productionDebug" || variant.name == "productionDebugMini") {
                variant.enable = false
            }
        }
    }
}

ktlint {
    version = libs.versions.ktlint.version
    verbose = true // useful for debugging
    android = true
    outputToConsole = true
    ignoreFailures = false

    reporters {
        reporter(ReporterType.PLAIN) // Output KtLint results in plain text format
        reporter(ReporterType.HTML) // Output KtLint results in HTML format
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    // AppCompat
    implementation(libs.androidx.appcompat)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.activity)
    implementation(libs.compose.animation)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material.ripple)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3.window.size)
    implementation(libs.compose.runtime)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.constraint.layout)
    // implementation(libs.lottie.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Utility
    implementation(libs.android.lint.rules)
    implementation(libs.timber)
    implementation(libs.accompanist.permissions)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    // Firebase
//    implementation(project.dependencies.platform(libs.firebase.bom))
//    implementation(libs.firebase.config)
//    implementation(libs.firebase.analytics)

    // Firebase open source
    // implementation(libs.firebase.open.source.config)
    // implementation(libs.compose.constraint.layout)
    // implementation(libs.compose.constraint.layout.android)
    // implementation(libs.compose.constraint.layout.core)
    // implementation(libs.androidx.constraintlayout)

    // Testing
    // androidTestImplementation(platform(libs.compose.bom))
    // androidTestImplementation(libs.compose.ui.test.junit4)
    // androidTestImplementation(libs.compose.ui.test.manifest)
    // androidTestImplementation(libs.androidx.test.core)
    // androidTestImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.truth)
    testImplementation(libs.koin.android.test)
    implementation(libs.koin)
    implementation(libs.koin.compose)
    implementation(libs.androidx.navigation)
    coreLibraryDesugaring(libs.core.library.desugaring)
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
