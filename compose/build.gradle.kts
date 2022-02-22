plugins {
    id(Config.ApplyPlugins.ANDROID_LIBRARY)
    kotlin(Config.ApplyPlugins.Kotlin.ANDROID)
    kotlin(Config.ApplyPlugins.Kotlin.KAPT)
    id(Config.ApplyPlugins.PARCELIZE)
}

android {
    compileSdk = Config.AndroidSdkVersions.COMPILE_SDK
    buildToolsVersion = Config.AndroidSdkVersions.BUILD_TOOLS

    defaultConfig {
        minSdk = Config.AndroidSdkVersions.MIN_SDK
        targetSdk = Config.AndroidSdkVersions.TARGET_SDK
        // As of AGP 7.0, versionName and versionCode have been removed from library modules: https://stackoverflow.com/a/67803541/201939
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true // Enables Jetpack Compose for this module
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Config.Compose.COMPOSE_COMPILER_VERSION
    }
    lint {
        // TODO: Remove once Timber is updated past 4.7.1 with AGP 7.0.0 related lint fixes: https://github.com/JakeWharton/timber/issues/408
        disable.addAll(
            setOf("LogNotTimber", "StringFormatInTimber", "ThrowableNotAtBeginning", "BinaryOperationInTimber", "TimberArgCount", "TimberArgTypes", "TimberTagLength", "TimberExceptionLogging")
        )
    }

    buildTypes {
        // Create debug minified buildtype to allow attaching debugger to minified build: https://medium.com/androiddevelopers/practical-proguard-rules-examples-5640a3907dc9
        create("debugMini") {
            initWith(getByName("debug"))
            setMatchingFallbacks("debug")
        }
    }
    flavorDimensions("environment")
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
    variantFilter {
        // Gradle ignores any variants that satisfy the conditions listed below. `productionDebug` has no value for this project.
        if (name == "productionDebug" || name == "productionDebugMini") {
            ignore = true
        }
    }
}

// Declare configurations per variant to use in the dependencies block below. More info: https://guides.gradle.org/migrating-build-logic-from-groovy-to-kotlin/#custom_configurations_and_dependencies
private val internalDebugImplementation: Configuration by configurations.creating { extendsFrom(configurations["debugImplementation"]) }
private val internalDebugMiniImplementation: Configuration by configurations.creating { extendsFrom(configurations["debugImplementation"]) }
private val internalReleaseImplementation: Configuration by configurations.creating { extendsFrom(configurations["releaseImplementation"]) }
val productionReleaseImplementation: Configuration by configurations.creating { extendsFrom(configurations["releaseImplementation"]) }

/** List of all buildable dev configurations */
val devConfigurations: List<Configuration> = listOf(internalDebugImplementation, internalDebugMiniImplementation, internalReleaseImplementation)

dependencies {

    // AndroidX
    composeDependencies()

    // Utility
    timberDependencies()

    // Test
    junitDependencies()
    mockitoKotlinDependencies()
    truthDependencies()
}
