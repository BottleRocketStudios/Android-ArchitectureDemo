import java.io.FileInputStream
import java.util.Properties

plugins {
    id(Config.ApplyPlugins.ANDROID_LIBRARY)
    id(Config.ApplyPlugins.JACOCO_ANDROID)
    kotlin(Config.ApplyPlugins.Kotlin.ANDROID)
    kotlin(Config.ApplyPlugins.Kotlin.KAPT)
    id(Config.ApplyPlugins.PARCELIZE)
}

jacoco {
    toolVersion = Config.JACOCO_VERSION
}

val apikey = ApiKeyProperties(System.getenv("APIKEY_PROPERTIES") ?: "apikey.properties", rootProject) // TODO: TEMPLATE - Remove this value when creating a new project

android {
    compileSdk = Config.AndroidSdkVersions.COMPILE_SDK
    buildToolsVersion = Config.AndroidSdkVersions.BUILD_TOOLS

    defaultConfig {
        minSdk = Config.AndroidSdkVersions.MIN_SDK
        targetSdk = Config.AndroidSdkVersions.TARGET_SDK
        // As of AGP 7.0, versionName and versionCode have been removed from library modules: https://stackoverflow.com/a/67803541/201939
        buildConfigField("String", "BITBUCKET_KEY", apikey.key)
        buildConfigField("String", "BITBUCKET_SECRET", apikey.secret)
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
    lint {

    }

    buildTypes {
        getByName("debug") {
            // Disabling as leaving it enabled can cause the build to hang at the jacocoDebug task for 5+ minutes with no observed adverse effects when executing
            // the jacocoTest...UnitTestReport tasks. Stopping and restarting build would allow compilation/installation to complete.
            // Disable suggestion found at https://github.com/opendatakit/collect/issues/3262#issuecomment-546815946
            isTestCoverageEnabled = false
        }
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

// TODO: TEMPLATE - Remove this class (and all its usages) when creating a new project
class ApiKeyProperties(pathToProperties: String, project: Project) {
    private val apikeyPropertiesFile = project.file(pathToProperties)
    private val apikeyProperties = Properties()

    init {
        apikeyProperties.load(FileInputStream(apikeyPropertiesFile))
    }

    val key: String
        get() = if (apikeyProperties["BITBUCKET_KEY"] is String) {
            apikeyProperties["BITBUCKET_KEY"] as String
        } else {
            throw(Exception("Unable to find BITBUCKET_KEY in apikey.properties"))
        }

    val secret: String
        get() = if (apikeyProperties["BITBUCKET_SECRET"] is String) {
            apikeyProperties["BITBUCKET_SECRET"] as String
        } else {
            throw(Exception("Unable to find BITBUCKET_SECRET in apikey.properties"))
        }
}

dependencies {
// TODO: Find a way to make sure we are aware of out-of-date versions of any static aars/jars in /libs. Manually check for any updates at/prior to dev signoff.
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // Kotlin/coroutines
    kotlinDependencies()
    coroutineDependencies()

    // AndroidX
    coreKtxDependencies()
    securityCryptoDependencies()

    koinDependencies()

    coreLibraryDesugaringDependencies()

    // Networking/parsing
    retrofitDependencies()
    moshiDependencies()

    // Utility
    liveEventDependencies()
    timberDependencies()
    commonsCodecDependencies()
    chuckerDependencies(devConfigurations = devConfigurations, productionConfiguration = productionReleaseImplementation)

    // Test
    junitDependencies()
    mockitoKotlinDependencies()
    truthDependencies()
    archCoreTestingDependencies()
    kotlinxCoroutineTestingDependencies()
    turbineDependencies()
}
