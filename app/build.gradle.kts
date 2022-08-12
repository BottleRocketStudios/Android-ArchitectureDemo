import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput

plugins {
    id(Config.ApplyPlugins.ANDROID_APPLICATION)
    kotlin(Config.ApplyPlugins.Kotlin.ANDROID)
    id(Config.ApplyPlugins.KSP)
    id(Config.ApplyPlugins.PARCELIZE)
    id(Config.ApplyPlugins.KARUMI_SHOT_TESTING)
}

extra.set("jacocoCoverageThreshold", 0.40.toBigDecimal()) // module specific code coverage verification threshold
apply(from = "../jacocoModule.gradle")

apply(from = "../renameAppBundle.gradle.kts") // configures additional gradle tasks to rename app bundles (when needed)

// Prep BuildInfoManager to use its functions/properties later throughout this build script
BuildInfoManager.initialize(
    BuildInfoInput(
        appVersion = AppVersion(major = 1, minor = 0, patch = 0, hotfix = 0, showEmptyPatchNumberInVersionName = true), // TODO: TEMPLATE - Replace with appropriate app version
        brandName = "BR_Architecture", // TODO: TEMPLATE - Replace with appropriate project brand name
        productionReleaseVariantName = "productionRelease",
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

        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
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
            // the test...UnitTestCoverage tasks. Stopping and restarting build would allow compilation/installation to complete.
            // Disable suggestion found at https://github.com/opendatakit/collect/issues/3262#issuecomment-546815946
            isTestCoverageEnabled = false
        }
        // Create debug minified buildtype to allow attaching debugger to minified build: https://medium.com/androiddevelopers/practical-proguard-rules-examples-5640a3907dc9
        create("debugMini") {
            initWith(getByName("debug"))
            matchingFallbacks += listOf("debug")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    flavorDimensions += listOf("environment")
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
    shot {
        applicationId = "com.bottlerocketstudios.brarchitecture.test"
        // tolerance = 1.1
    }
}

// Declare configurations per variant to use in the dependencies block below. See :data module for examples if needed here in the :app module.

dependencies {
    implementation(project(mapOf("path" to ":domain")))
    implementation(project(mapOf("path" to ":data")))
    implementation(project(mapOf("path" to ":compose")))
    // TODO: List out each jar/aar explicitly to help avoid the danger of someone "slipping" a dangerous lib into the directory
    // implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin/coroutines
    kotlinDependencies()
    coroutineDependencies()

    // Koin DI
    koinDependencies()

    // AndroidX
    composeDependencies()
    accompanistDependencies()
    appCompatDependencies()
    activityDependencies()
    androidxStartupDependencies()
    materialDependencies()
    lifecycleDependencies()
    navigationDependencies()

    // Launchpad
    launchPadDependencies()

    coreLibraryDesugaringDependencies()

    // Utility
    brCustomAndroidLintRules()
    liveEventDependencies()
    timberDependencies()
    processPhoenixDependencies()
    leakCanaryDependencies()

    // Test
    junitDependencies()
    mockitoKotlinDependencies()
    truthDependencies()
    archCoreTestingDependencies()
    kotlinxCoroutineTestingDependencies()
    turbineDependencies()
    // Android Test
    espressoDependencies()
    extJunitRunnerDependencies()
    androidxCoreDependencies()
    composeUITestingDependencies()
}
