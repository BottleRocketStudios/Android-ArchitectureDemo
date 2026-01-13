import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.kotlinSerialization)
    // id(Config.ApplyPlugins.GOOGLE_SERVICES)
}

// extra.set("jacocoCoverageThreshold", 0.30.toBigDecimal()) // module specific code coverage verification threshold
// apply(from = "../jacocoModule.gradle")

val apikey = ApiKeyProperties(System.getenv("APIKEY_PROPERTIES") ?: "apikey.properties", rootProject) // TODO: TEMPLATE - Remove this value when creating a new project

android {
    namespace = libs.versions.data.namespace.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        lint.targetSdk = libs.versions.android.targetSdk.get().toInt()
        buildConfigField("String", "BITBUCKET_KEY", apikey.key)
        buildConfigField("String", "BITBUCKET_SECRET", apikey.secret)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    }

    buildTypes {
        getByName("debug") {
            // Disabling as leaving it enabled can cause the build to hang at the jacocoDebug task for 5+ minutes with no observed adverse effects when executing
            // the test...UnitTestCoverage tasks. Stopping and restarting build would allow compilation/installation to complete.
            // Disable suggestion found at https://github.com/opendatakit/collect/issues/3262#issuecomment-546815946
            enableUnitTestCoverage = false
        }
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
    implementation(project(":domain"))
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)

    // Koin
    implementation(libs.koin)
    implementation(libs.timber)

    // Ktor HTTP client
    implementation(libs.ktor)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.logging)

    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.chucker)
    implementation(libs.base64)

    // Google Credential Manager
    implementation(libs.google.credentials)
    implementation(libs.google.credentials.play.services.auth)

    // DataStore & Crypto
    implementation(libs.datastore.preferences)
    implementation(libs.tink.android)

    // Firebase
    implementation(project.dependencies.platform(libs.firebase.bom))
    implementation(libs.firebase.config)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.perf)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
