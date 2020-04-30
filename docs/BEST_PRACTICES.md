Best Practices
==============

## Summary
The point of this doc is to share some best practices and processes within the team so that we are all on the same page regarding the points listed.

## Target Audience
Dev team :)

### IDE
* Use the Code Style settings provided by the project. Note that the codestyle has been checked into git.
![Code style dialog][code-style-dialog]

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
[ide_json_string_no_language_injection]:images/ide_json_string_no_language_injection.png
[ide_json_string_with_language_injection]:images/ide_json_string_with_language_injection.png
[ide_json_string_with_language_injection_showing_error]:images/ide_json_string_with_language_injection_showing_error.png
[ide_html_string_with_language_injection]:images/ide_html_string_with_language_injection.png
[kdoc]:https://kotlinlang.org/docs/reference/kotlin-doc.html
[cloc]:https://github.com/AlDanial/cloc