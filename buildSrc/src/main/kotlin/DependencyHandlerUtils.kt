import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * For whatever reason, I'm unable to get `Dependencies.kt` to recognize the `DependencyHandler` syntax sugar functions normally used in the dependencies block of a build.gradle file (as seen below).
 * So I Cmd+Clicked into each one from the app module build.gradle dependencies block and copy/pasted them here so they are accessible to `Dependencies.kt`
 * More info in BEST_PRACTICES.md -> Build section
 */
private val fileKDoc = Unit

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.`implementation`(dependencyNotation: Any): Dependency? = add("implementation", dependencyNotation)

/**
 * Adds a dependency to the 'kapt' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.`kapt`(dependencyNotation: Any): Dependency? = add("kapt", dependencyNotation)

/**
 * Adds a dependency to the 'ksp' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.`ksp`(dependencyNotation: Any): Dependency? = add("ksp", dependencyNotation)

/**
 * Adds a dependency to the 'debugImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.`debugImplementation`(dependencyNotation: Any): Dependency? = add("debugImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'releaseImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.`releaseImplementation`(dependencyNotation: Any): Dependency? = add("releaseImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'testImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.`testImplementation`(dependencyNotation: Any): Dependency? = add("testImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'androidTestImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.`androidTestImplementation`(dependencyNotation: Any): Dependency? = add("androidTestImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'api' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.`api`(dependencyNotation: Any): Dependency? = add("api", dependencyNotation)

/**
 * Adds a dependency to the 'coreLibraryDesugaring' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.`coreLibraryDesugaring`(dependencyNotation: Any): Dependency? = add("coreLibraryDesugaring", dependencyNotation)
