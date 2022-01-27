Best Practices
==============

## Summary
The point of this doc is to share some best practices and processes within the team so that we are all on the same page regarding the points listed.

## Target Audience
Dev team :)

## IDE
1. **Use the Code Style settings provided by the project**. Note that the codestyle has been checked into git.
![Code style dialog][code-style-dialog]
2. **Use the Run Configurations checked into project (via the Store as project file checkbox)**. There are a ton checked into the project and available for use (ss might not represent the latest set of Run Configurations).
![Shared run configurations][shared-run-configurations]

### Run Configurations Explained
*Relative Importance Key*

* High - Task expected to be used very frequently.
* Medium - Task expected to be used occasionally.
* Low - Task available but not expected to be used very frequently.

| Run Configuration                                                    | Relative Importance? | Typical execution cadence?            | Description                                                                                                                                                                  | Use Case                                                                                                                                                                                                                                                       |
|----------------------------------------------------------------------|----------------------|---------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `bundleInternalDebug`                                                | Low                  |                                       | Executes the gradle task to create an `internalDebug` variant app bundle                                                                                                     | Validate app bundle built locally.                                                                                                                                                                                                                             |
| `bundleProductionRelease CI test w/ fake data`                       | Low                  |                                       | Executes the gradle task to create a `productionRelease` variant app bundle **as if run on CI** using fake data specified in the Run Configuration environment variables     | Validate app bundle renaming logic as if built by CI.                                                                                                                                                                                                          |
| `bundleProductionRelease non-CI test w/ fake data`                   | Low                  |                                       | Executes the gradle task to create a `productionRelease` variant app bundle **as if NOT run on CI** using fake data specified in the Run Configuration environment variables | Validate app bundle renaming logic as if built locally.                                                                                                                                                                                                        |
| `detekt`                                                             | Low                  |                                       | Executes the detekt gradle task to perform static analysis. Build will fail on detekt failure.                                                                               | Run locally to ensure build won't fail on CI. Remediate issues found until task executes successfully.                                                                                                                                                         |
| `ktlintCheckDetekt`                                                  | Low                  |                                       | Executes both ktlint check and detekt gradle tasks.                                                                                                                          | Run locally to ensure build won't fail on CI. Remediate issues found until task executes successfully.                                                                                                                                                         |
| `testInternalDebugCombinedUnitTestCoverage`                          | Medium               | Viewing/increasing unit test coverage | Executes a custom jacoco gradle task that generates a combined jacoco report of both `:app` and `:data` modules (in addition to the separate reports per module)             | When desiring to increase unit test coverage, you can execute this task followed by `Open Jacoco Report - internalDebug` to view existing coverage, write new unit tests to increase coverage, and repeat the process to view/validate the coverage increases. |
| `testInternalDebugUnitTestCoverage`                                  | Low                  |                                       | Executes a custom jacoco gradle task that generates separate jacoco reports per module. Prefer executing `testInternalDebugCombinedUnitTestCoverage` instead.                | Prefer executing `testInternalDebugCombinedUnitTestCoverage` instead.                                                                                                                                                                                          |
| `testInternalDebugUnitTestCoverageVerification`                      | Low                  |                                       | Executes a custom jacoco gradle task that fails the build when the minimum code coverage threshold is NOT met (per module).                                                  | Run locally to ensure that the build won't fail due to minimum code coverage thresholds NOT being met.                                                                                                                                                         |
| `ktlintFormat`                                                       | High                 | Prior to pushing to remote            | Executes ktlint format gradle task to automatically fix most ktlint errors. Manual error resolution might still be required.                                                 | Run locally prior to pushing local commits to the remote git repository to automatically fix most ktlint errors. Remediate issues found until task executes successfully.                                                                                      |
| `lintInternalDebug`                                                  | Low                  |                                       | Executes android lint gradle task. Build will fail on lint error.                                                                                                            | Run locally to ensure build won't fail on CI. Remediate issues found until task executes successfully.                                                                                                                                                         |
| `dependencyUpdates`                                                  | High                 | When updating project dependencies    | Executes dependency updates gradle task which reports which dependencies have updates available.                                                                             | Run to see what dependency updates are available when desiring to update dependencies.                                                                                                                                                                         |
| `:app - testInternalDebugUnitTest`                                   | Low                  |                                       | Executes all `:app` module unit tests                                                                                                                                        | Run to validate success/failure of all `:app` module unit tests with AS unit test run UI.                                                                                                                                                                      |
| `:data - testInternalDebugUnitTest`                                  | Low                  |                                       | Executes all `:data` module unit tests                                                                                                                                       | Run to validate success/failure of all `:data` module unit tests with AS unit test run UI.                                                                                                                                                                     |
| `ci - internalDebug (minus clean) - dev build`                       | High                 | Prior to pushing to remote            | Executes all gradle tasks that would be run on CI for the `internalDebug` variant. If this task succeeds, there is a 99.9% chance the build will succeed on CI.              | Run locally to ensure build won't fail on CI prior to pushing local commits to the remote git repository. Remediate issues found until task executes successfully.                                                                                             |
| `ci - internalRelease (minus clean) - qa build`                      | Low                  |                                       | Executes all gradle tasks that would be run on CI for the `internalRelease` variant. If this task succeeds, there is a 99.9% chance the build will succeed on CI.            | Run locally to ensure build won't fail on CI prior to pushing local commits to the remote git repository. Remediate issues found until task executes successfully.                                                                                             |
| `ci - productionRelease (minus clean) - CI store build w/ fake data` | Low                  |                                       | Executes all gradle tasks that would be run on CI for the `productionRelease` variant. If this task succeeds, there is a 99.9% chance the build will succeed on CI.          | Run locally to ensure build won't fail on CI prior to pushing local commits to the remote git repository. Remediate issues found until task executes successfully.                                                                                             |
| `ktlintCheck`                                                        | Low                  |                                       | Executes ktlint check task to show any ktlint code style/formatting errors.                                                                                                  | Run locally to ensure build won't fail on CI. Remediate issues found until task executes successfully.                                                                                                                                                         |
| `Open Jacoco Report - internalDebug`                                 | Medium               | Viewing/increasing unit test coverage | Executes shell script to open the `internalDebug` variant html jacoco reports (unified, `:app`, and `:data`) in your system's default browser                                | After executing any `ci - *` or `testInternalDebugCombinedUnitTestCoverage` task, run this task to launch the jacoco reports for viewing.                                                                                                                      |

### .idea folder values checked into the git repo
* `.idea/codeStyles/*` - This folder contains all of the shared project codestyle files and is crucial to ensure the whole team is using the same code style.
* `.idea/runConfigurations/*`- This folder contains all of the shared project run configurations. To share a Run Configuration with the team, check the `Store as project file` checkbox in the Edit Run Configuration window.

## Build
### Using dependency configurations in `buildSrc/.../Dependencies.kt`
As you add new dependencies, you might run across a configuration not supported in the Dependencies.kt `fun DependencyHandler.fooDependencies() {...}` function body (such as `compileOnly`). If you cmd+click on any existing configuration (such as `deubgImplementation` or `api`), you will see that the implementation actually lives in `DependencyHandlerUtils.kt`. This was copy/pasted from the source file that backs the configuration present in a standard build.gradle.kts dependencies block that is unfortunately inaccessible in `buildSrc` kotlin files. These configurations must be brought in as mentioned below.

### Adding a new configuration
Here is an example flow to add a new configuration to `DependencyHandlerUtils.kt`

1. In `Dependencies.kt`, you want to add the following block but see a compilation error (`unresolved reference: compileOnly`):

```
fun DependencyHandler.fooDependencies() {
    compileOnly(Libraries.FOO)
}
```

2. Copy/paste `compileOnly(Libraries.FOO)` to the `:app` build.gradle.kts dependencies block and cmd+click on `compileOnly`
3. Copy the `compileOnly` extension function source and paste into `DependencyHandlerUtils.kt`
4. Remove the line from the `:app` build.gradle.kts (in step 2)
5. Navigate back to`Dependencies.kt` (step 1) and observe the block has no compilation error.
6. You're done!

## Code
### Style
#### ktlint
Kotlin linter for kotlin code against the android kotlin style guide to guard against style violations that should be run on the CI and fails builds on a `ktlintCheck` error.

To prevent ci failures, your development process should include running `ktlintFormat` prior to pushing any commits to the remote. Example below:

1. Work on feature/bug making commits until you are ready to push your changes.
2. Run `ktlintFormat` and manually fix any issues.
3. Commit changes (if any).
4. Push branch (new or updates) to the remote.

Useful gradle tasks that come with the gradle plugin:

```
// Verification/auto-format
./gradlew ktlintCheck
./gradlew ktlintFormat

// Kotlinscript only verification/format (not very useful)
./gradlew ktlintKotlinScriptCheck
./gradlew ktlintKotlinScriptFormat

// Adds Git pre-commit hook, that runs ktlint check over staged files. (optional)
./gradlew addKtlintCheckGitPreCommitHook

// Adds Git pre-commit hook, that runs ktlint format over staged files and adds fixed files back to commit (optional)
./gradlew addKtlintFormatGitPreCommitHook
```

More info at https://github.com/JLLeitschuh/ktlint-gradle#configuration

#### General
* **Do not use Hungarian notation!**. This means no `mUser` or `sUserManager`. Instead try `user` and `userManager`.

#### Sealed Classes
* Use PascalCase (aka UpperCamelCase) **for all objects/classes that are part of (aka extend) a sealed class**. Do not use `SCREAMING_SNAKE_CASE` here!

```kotlin
sealed class Foo {
    object Loading : Foo()
    data class Error(val error: CustomError) : Foo()
    data class Bar(val baz: String) : Foo()
}
```

##### References
* Kotlin naming rules: http://kotlinlang.org/docs/reference/coding-conventions.html#naming-rules (shows examples for object)
* Sealed class api docs: https://kotlinlang.org/docs/reference/sealed-classes.html (shows PascalCase/UpperCamelCase for objects in sealed class)

### FIXME/TODO Comments
* Use `FIXME` comments to leave "breadcrumbs" in cases where some specific code or condition needs to be revisited **before the next release**. You might leave a FIXME when using a placeholder value that needs to be updated with the real one later on down the road when it is created/obtained. During dev signoff, all FIXME entries should be evaluated and handled appropriately.
    * `// FIXME: Replace with production value from client before release!`
    * `// FIXME: Set to BuildConfig.DEBUG before release!`
* Use `TODO` comments to leave "breadcrumbs" of ideas to improve the codebase. You might call out code that you write that could be done in a more optimal way in the future. Or you could leave a TODO for some code that you see that could be improved but you aren't able to immediately act on it right then.
    * `// TODO: TODO: Consider breaking this up into smaller mappers if the scope becomes too large`
    * `// TODO: Remove this if able after simplifying search results logic`

#### Tip
To display a helpful viewer for TODO and FIXME tags in Android Studio, go to Navigate -> Tool Windows -> TODO

### Template Items
There are files in the project marked with either `<!-- TEMPLATE: ... -->` or `// TEMPLATE: ...`. You can copy whole files or snippets marked with these tags as a basis for a new feature/screen/etc. The point of these existing is to provide a skeleton implementation of a thing(feature/screen/etc) to help speedup development and provide a common/somewhat uniform baseline expectation. To find them easily, you can use Find in Path -> TEMPLATE: or add a filter for TEMPLATE in View -> Tools Windows -> TODO.

### [KDoc][kdoc]
* Add KDoc headers to new interfaces/classes that you create to define what it represents and what it does. 
* Add KDoc to non-trivial methods explaining expected input and output where appropriate.

### Language Injections
* Use the @Language("JSON") annotation to add syntax highlighting/editing assistance/error handling to string literals.
* Definitely use for **JSON** and **HTML** string literals in the app.

##### No language injection
![ide_json_string_no_language_injection]

##### With language injection you have nice syntax highlighting ...
![ide_json_string_with_language_injection]

##### ... and also language specific error handling
![ide_json_string_with_language_injection_showing_error]

##### HTML with language injection
![ide_html_string_with_language_injection]

* Full documentation at https://www.jetbrains.com/help/idea/using-language-injections.html#language_annotation


### TimeUnit suffixes
It is a good practice to add the appropriate Time Unit suffix to methods, parameters, properties, and variables on native types such as `Long`/`Int`. This makes it explicitly clear what the value actually represents.

|Type|GOOD (Clear meaning)|BAD (Ambiguous; have to drill through usages to interpret what value it represents)|
|---|---|---|
|variable|`progressMs` **OR** `progressSeconds`|`progress`|
|property|`durationMs` **OR** `durationSeconds`|`duration`|
|method|`positionMs()` **OR** `positionSeconds()`|`position()`|
|parameter|`fun foo(positionMs: Long, durationMs: Long) {}`|`fun foo(position: Long, duration: Long) {}`|

If using a type that abstracts the need for a unit such as `org.threeten.bp.Duration`, then you don't need to specify a suffix on that object. If you pull out the ms value of the duration as a variable, then add the suffix.

## Use `StringIdHelper` to avoid depending on `Context` 
Use `StringIdHelper.*` types (representing string resources, format strings, and plurals as well as raw strings) in `ViewModel`s (or other classes) to avoid needing a `Context`. This allows for **simpler unit testing** and **prevents the need for `Context` in places it shouldn't be used** by pushing the resolution of the final String to the view layer (using the `textByStringIdHelper` DataBinding custom BindingAdapter function that takes in a `Context`).

## Inject `Clock`
Pass the `java.time.Clock` instance from Koin to any java 8 date/time apis that can use it in order **to allow control of the clock in unit tests**. This will typically be the `.now(clock: Clock)` api on `Instant`, `ZonedDateTime`, `LocalDateTime`, `LocalDate`, `LocalTime`, and so on.

## Jacoco
1. **Generate a combined jacoco report of all modules (as well as a report per module)** by executing the `testInternalDebugCombinedUnitTestCoverage` gradle task (or included Android Studio Run Configuration). More info in [jacocoRoot.gradle](../jacocoRoot.gradle)
    * **View the generated reports** by executing the `Open Jacoco Report - internalDebug` Android Studio Run configuration, manually executing `open_jacoco_report_internalDebug.sh`, or manually opening the index.html files from the reports directories.
    * You can also just generate the report per module (without the combined report) by executing `testInternalDebugUnitTestCoverage` (or included Android Studio Run Configuration). More info in [jacocoModule.gradle](../jacocoModule.gradle)
2. **Fail builds that don't meet the minimum code coverage thresholds per module** by executing the `testInternalDebugUnitTestCoverageVerification` gradle task (or included Android Studio Run Configuration). More info in [jacocoModule.gradle](../jacocoModule.gradle)

## Modularization
Currently the app is broken into two modules: `app` and `data`

### app module
* The top level module.
* Uses the `com.android.application` gradle plugin.

#### What should be in it
* Activity
* Fragment
* ViewModels
* Android resources such as strings/layouts/nav_graphs/drawables/colors/styles/themes/etc
* DI UI Modules
* DI Graph creation

### data module
* The base module.
* Uses the `com.android.library` gradle plugin.
* `app` depends on `data`.
* Make liberal use of the `internal` modifier where applicable to prevent leaking implementation details from `data` to modules that consume it (or are dependent on it).

#### What should be in it
* Repositories
* Models
* Networking
* Database
* General Utilities including top level functions and objects
* DI Data/Networking/Domain Modules
* **Minimal (or nonexistent) use of context and other android framework apis**

#### What should not be in it
* Any Android Resources (including usage of Android Resources)

### Testing Notes
* Test source is isolated per module. For example, `app` doesn't inherit `BaseTest` defined in `data`.
* Any testing specific classes (such as `BaseTest`) needs to be duplicated for each module that needs it.

## Build Types/Variants Table
<!-- TODO: TEMPLATE - Replace with appropriate base applicationIds (leave .internal and .internal.debug) -->

| Variant           | Application ID                                            | Dev use?                                                                                                                             | Dev functionality (environment picker, dev options screen, etc)? | Logging (logcat)? | Proxyable? (Charles) | Debuggable? | Signing Keystore? | Proguard/R8? | Built by CI (Jenkins)? | Artifacts Stored (Artifactory/AppCenter)? | QA use?                                              |
|-------------------|-----------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------|-------------------|----------------------|-------------|-------------------|--------------|------------------------|-------------------------------------------|------------------------------------------------------|
| internalDebug     | **com.bottlerocketstudios.brarchitecture.internal.debug** | Primary local dev machine build                                                                                                      | Enabled                                                          | Enabled           | Yes                  | Yes         | Debug             | No           | No                     | No                                        | Rarely/never                                         |
| internalDebugMini | **com.bottlerocketstudios.brarchitecture.internal.debug** | Use this variant to enable proguard/R8 (minification/obfuscation) on a debug build. Attaching debugger only allowed on debug builds. | Enabled                                                          | Enabled           | Yes                  | Yes         | Debug             | Yes          | No                     | No                                        | Never                                                |
| internalRelease   | **com.bottlerocketstudios.brarchitecture.internal**       | Dev doesn't have much use for this variant but can use for proguard/R8 testing if attaching a debugger on device is not needed.      | Enabled                                                          | Enabled           | Yes                  | No          | Release           | Yes          | Yes                    | Yes                                       | Primary variant for QA testing                       |
| productionRelease | **com.bottlerocketstudios.brarchitecture**                | Smoketest before release                                                                                                             | Disabled                                                         | Disabled          | No                   | No          | Release           | Yes          | Yes                    | Yes                                       | Primary after Feature Complete and until app release |

*Generated with https://www.tablesgenerator.com/markdown_tables#*

### Dev Highlights
* Use the `internalDebugMini` variant to build a proguarded/R8 debuggable build for local testing/debugging.
* Use the `internalDebug` variant for day to day development.
* Use the `internalRelease` variant to build the version QA tests. Can also use CI built apk.
* Use the `productionRelease` variant to smoke test that all dev options are disabled (environment picker, etc) and the app is not proxyable. Can also use CI built apk.

## Code (Project Specific Items)
Add when things come up.

## Gradle Kotlin DSL

### General Resources
* https://github.com/gradle/kotlin-dsl
* https://docs.gradle.org/current/userguide/kotlin_dsl.html
* https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources


### Tips for migrating from groovy scripts to kotlin gradle script
* Full guide: https://guides.gradle.org/migrating-build-logic-from-groovy-to-kotlin/
#### Takeaways
* Change all single quotes to double quotes
* Add parentheses in appropriate places for getters and = or () for setters
* Ensure groovy scripts still compile
* Change filename suffix from .gradle to .gradle.kts and sync -> manually tweak script to eliminate errors

### Api
* Full kotlin gradle dsl api: https://gradle.github.io/kotlin-dsl-docs/api/org.gradle.kotlin.dsl/index.html
* General kotlin build script api info at https://gradle.github.io/kotlin-dsl-docs/api/org.gradle.kotlin.dsl/-kotlin-build-script/index.html
* General kotlin settings script api info at https://gradle.github.io/kotlin-dsl-docs/api/org.gradle.kotlin.dsl/-kotlin-settings-script/index.html

### Default Build Script imports
* https://docs.gradle.org/current/userguide/writing_build_scripts.html#script-default-imports

### Dealing with task retrieval/creation
Takeaways:

* Use delegation apis available for extras/properties set/get
* Don't use map syntax for extra set/get (see below):

> There is one last syntax for extra properties that we should cover, one that treats extra as a map. We recommend against using this in general as you lose the benefits of Kotlin’s type checking and it prevents IDEs from providing as much support as they could. However, it is more succinct than the delegated properties syntax and can reasonably be used if you only need to set the value of an extra property without referencing it later.

* https://docs.gradle.org/current/userguide/kotlin_dsl.html#kotdsl:containers

### Interoperability
* Tips/info on dealing with groovy <-> kotlin build script nuances/syntax: https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:interoperability
* Groovy interop sample: https://github.com/gradle/kotlin-dsl/tree/master/samples/groovy-interop

### Limitations
* https://docs.gradle.org/current/userguide/kotlin_dsl.html#kotdsl:limitations
* Don't use `org.gradle.configureondemand=true` (see below):

> We recommend against enabling the incubating configuration on demand feature as it can lead to very hard-to-diagnose problems.

### Dependencies.kt
* https://antonioleiva.com/kotlin-dsl-gradle/
* https://proandroiddev.com/migrating-android-build-scripts-from-groovy-to-kotlin-dsl-f8db79dd6737
* https://handstandsam.com/2018/02/11/kotlin-buildsrc-for-better-gradle-dependency-management/

## Miscellaneous

### Viewing/Editing shared preferences (using AS Device File Explorer)
See https://stackoverflow.com/a/52352741/201939 as well as comments to see how to save as/upload edits on top of the original files.

### Inspect/Query/Modify databases (using AS App Inspection / Database Inspector on api 26+ device/emulator)
See https://developer.android.com/studio/inspect/database

### Counting lines of code (using [cloc][cloc])
* Install using homebrew: `brew install cloc` (or your package manager of choice: https://github.com/AlDanial/cloc#install-via-package-manager)
* Execute the following snippet via command line from the root project directory:
```bash
# From root project directory:
cloc app/src --by-file-by-lang
```

## Other Useful General BR Pages
* [Android Coding Standards - Naming Conventions](https://confluence.bottlerocketapps.com/display/BKB/Android+Coding+Standards#AndroidCodingStandards-NamingConventions)
* [Creating Time Intervals](https://confluence.bottlerocketapps.com/display/BKB/Creating+Time+Intervals)
* [Simulate OS Killing your app](https://confluence.bottlerocketapps.com/display/BKB/Simulate+OS+Killing+your+app)
* [Android System WebView Findings](https://confluence.bottlerocketapps.com/display/BKB/Android+System+WebView+Findings)

[code-style-dialog]:images/android_studio_code_style_dialog_ss.png
[shared-run-configurations]:images/android_studio_shared_run_configurations.png
[ide_json_string_no_language_injection]:images/ide_json_string_no_language_injection.png
[ide_json_string_with_language_injection]:images/ide_json_string_with_language_injection.png
[ide_json_string_with_language_injection_showing_error]:images/ide_json_string_with_language_injection_showing_error.png
[ide_html_string_with_language_injection]:images/ide_html_string_with_language_injection.png
[kdoc]:https://kotlinlang.org/docs/reference/kotlin-doc.html
[cloc]:https://github.com/AlDanial/cloc