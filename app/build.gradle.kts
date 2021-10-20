import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput

plugins {
    id(Config.ApplyPlugins.ANDROID_APPLICATION)
    id(Config.ApplyPlugins.JACOCO_ANDROID)
    kotlin(Config.ApplyPlugins.Kotlin.ANDROID)
    kotlin(Config.ApplyPlugins.Kotlin.KAPT)
    id(Config.ApplyPlugins.PARCELIZE)
    id(Config.ApplyPlugins.NAVIGATION_SAFE_ARGS_KOTLIN)
}

jacoco {
    toolVersion = Config.JACOCO_VERSION
}

// Prep BuildInfoManager to use its functions/properties later throughout this build script
BuildInfoManager.initialize(
    BuildInfoInput(
        appVersion = AppVersion(major = 1, minor = 0, patch = 0, hotfix = 0, showEmptyPatchNumberInVersionName = true), // TODO: TEMPLATE - Replace with appropriate app version
        brandName = "BR_Architecture", // TODO: TEMPLATE - Replace with appropriate project brand name
        productionReleaseVariantName = "release",
        rootProjectDir = rootDir
    )
)

// Some documentation on inner tags/blocks can be found with the below urls:
// android {...} DSL Reference:
// Android Gradle Plugin api: https://developer.android.com/reference/tools/gradle-api/4.1/classes
android {
    compileSdk = Config.AndroidSdkVersions.COMPILE_SDK
    buildToolsVersion = Config.AndroidSdkVersions.BUILD_TOOLS
    defaultConfig {
        minSdk = Config.AndroidSdkVersions.MIN_SDK
        targetSdk = Config.AndroidSdkVersions.TARGET_SDK
        versionCode = BuildInfoManager.APP_VERSION.versionCode
        versionName = BuildInfoManager.APP_VERSION.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    lint {
        // TODO: Remove once Timber is updated past 4.7.1 with AGP 7.0.0 related lint fixes: https://github.com/JakeWharton/timber/issues/408
        disable.addAll(
            setOf("LogNotTimber", "StringFormatInTimber", "ThrowableNotAtBeginning", "BinaryOperationInTimber", "TimberArgCount", "TimberArgTypes", "TimberTagLength", "TimberExceptionLogging")
        )
    }
    signingConfigs {
        getByName("debug") {
            // Common debug keystore so all local builds can be shared between devs/QA
            storeFile = file("../keystore/debug.keystore") // TODO: TEMPLATE - Generate a new debug keystore (optional). More info in NEW_PROJECT_STARTER.md
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            // Release keystore expected to be present in environment variables (living on the build server)
            storeFile = file(System.getenv("_KEYSTORE") ?: "_KEYSTORE environment variable not set for release build type; unable to compile the current variant")
            storePassword = System.getenv("_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("_KEY_ALIAS")
            keyPassword = System.getenv("_KEY_PASSWORD")
        }
    }
    // See BEST_PRACTICES.md for comments on purpose of each build type/flavor/variant
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            // Disabling as leaving it enabled can cause the build to hang at the jacocoDebug task for 5+ minutes with no observed adverse effects when executing
            // the jacocoTest...UnitTestReport tasks. Stopping and restarting build would allow compilation/installation to complete.
            // Disable suggestion found at https://github.com/opendatakit/collect/issues/3262#issuecomment-546815946
            isTestCoverageEnabled = false
        }
        // Create debug minified buildtype to allow attaching debugger to minified build: https://medium.com/androiddevelopers/practical-proguard-rules-examples-5640a3907dc9
        create("debugMini") {
            initWith(getByName("debug"))
            setMatchingFallbacks("debug")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    flavorDimensions("environment")
    // See BEST_PRACTICES.md for comments on purpose of each build type/flavor/variant
    productFlavors {
        create("internal") {
            applicationId = "com.bottlerocketstudios.brarchitecture.internal" // TODO: TEMPLATE - Replace with appropriate project applicationId prefix, leaving .internal
            versionNameSuffix = "-internal"
            buildConfigField("boolean", "INTERNAL", "true")
            buildConfigField("boolean", "PRODUCTION", "false")
            dimension = "environment"
        }
        create("production") {
            applicationId = "com.bottlerocketstudios.brarchitecture" // TODO: TEMPLATE - Replace full string with appropriate project applicationId
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
    applicationVariants.all {
        // Using a local val here since attempting to use a named lambda parameter would change the function signature from operating on applicationVariants.all (with an `Action` parameter)
        // to the Collections Iterable.`all` function. Same thing applies to outputs.all below
        val variant: ApplicationVariant = this
        BuildInfoManager.createBuildIdentifier(variant)
        variant.outputs.all {
            val baseVariantOutput: BaseVariantOutput = this
            BuildInfoManager.modifyVersionNameAndApkName(variant, baseVariantOutput)
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
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
    implementation(project(mapOf("path" to ":data")))
    // TODO: Find a way to make sure we are aware of out-of-date versions of any static aars/jars in /libs. Manually check for any updates at/prior to dev signoff.
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin/coroutines
    kotlinDependencies()
    coroutineDependencies()

    // AndroidX
    appCompatDependencies()
    androidxStartupDependencies()
    constraintLayoutDependencies()
    materialDependencies()
    lifecycleDependencies()
    navigationDependencies()

    koinDependencies()

    coreLibraryDesugaringDependencies()

    // UI
    groupieDependencies()
    glideDependencies()

    // Utility
    liveEventDependencies()
    timberDependencies()
    processPhoenixDependencies()
    leakCanaryDependencies()
    debugDatabaseDependencies(devConfigurations = devConfigurations)

    // Test
    junitDependencies()
    mockitoKotlinDependencies()
    truthDependencies()
    archCoreTestingDependencies()
    kotlinxCoroutineTestingDependencies()
    // Android Test
    espressoDependencies()
}
