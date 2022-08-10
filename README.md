# Bottle Rocket Android Architecture Guidelines

## How to Open Project in Android Studio
* Clone project to local machine
* Open project in Android Studio
* If prompted, select "Default Gradle Wrapper"
* Project should open and Gradle should sync without any issues.

## OAuth consumer key creation and setup steps
* Generate a Bitbucket OAuth consumer key at <https://bitbucket.org/[your-bitbucket-username]/workspace/settings/api>
    * You must specify a callback url with the following value for auth code login to work: <https://www.bottlerocketstudios.com/>
    * You must enable the following permissions (at a minimum) to ensure proper functioning of the app:
        * **Account** read
        * **Snippets** read/write
        * **Repositories** read
* Create a file in project root named "apikey.properties" with following format:

```
BITBUCKET_KEY="[oauth_consumer_key]"
BITBUCKET_SECRET="[oauth_consumer_secret]"
```

## Primary Docs
* **Required reading** - [`NEW_PROJECT_STARTER.md`](./docs/NEW_PROJECT_STARTER.md) for information on how to use this repository as a new project starter.
* **Required reading** - [`BEST_PRACTICES.md`](./docs/BEST_PRACTICES.md) for Android engineering team norms for the project.

## Pre-PR Creation Checklist
1. Create 1+ commits locally until work is completed.
2. Execute the Android Studio `ci - internalDebug (minus clean) - dev build` Run Configuration (via `Run` -> `Run...` or `Ctrl+Opt+R` on macOS)
    * Fix any failures that surface due to linting, compilation, or unit tests.
3. Build/install the `internalDebugMini` variant on your testing emulator/device (enables proguard and will crash if proguard rules need tweaking but weren't)
4. Smoke test the app and ensure no regressions/crashes/unexpected behavior.
5. Create PR!

## Updating Dependencies
The project is using the Gradle Versions plugin to do a lot of heavy lifting here!

1. Execute the `dependencyUpdates` task to view third party dependency updates via `./gradlew dependencyUpdates` or AS Gradle -> [project]] -> Tasks -> help -> dependencyUpdates.
2. View the generated report that mentions any library dependency updates (as well as Gradle updates). 
3. For each dependency update list:
    1. View the release notes to see relevant bug fixes, breaking changes (api modification, removal or replacement), and deprecations
    2. Update the version
    3. Make any necessary changes to code, build files, and/or unit tests.
    4. Follow steps in the Pre-PR Creation Checklist, especially ensuring the app still compiles and is smoke tested with the new changes.

*Note that you'll still need to manually check for dependency updates on all buildscript plugins (see `Dependencies.kt` -> `Config.BuildScriptPlugins` properties), including the Gradle Versions plugin.*

## Adding Tabs to the Nav Drawer
This project uses the `generateNavDrawerItems` method in the `ComposeActivity` to draw items in the Nav Drawer.

1. Prepare the ingredients for your new NavItemState
    1. Prepare an Icon for your NavItemState
    2. Create a string resource for your NavItemState's name
    3. Setup a Route in the Routes.kt for your NavItemState to navigate to
    4. Determine if your NavItemState will have a nested list
2. Go to the `ComposeActivity` and locate the listOf(...) in `GenerateNavDrawerItems`
3. Create your new NavItemState using the ingredients gathered
4. Add it to the list in the order required
5. If your NavItemState includes a nested list
    1. Determine the criteria for which the nested menu appears (e.g Only show the nested menu when a repo is selected)
    2. Set the nestedMenuItems variable in NavItemState to a listOf(NavItemState) made up of your nested menu items
    3. The padding and styling of the nested list are handled by `NavDrawer.kt`, modify to requirements

## Future functionality
*Note: Evaluate if the implementing something in this list is still best practice prior to coding it.*

*Made up example: If kotlin releases native date/time support (and becomes the go to date time solution for Android), don't implement java 8 date/time libs if it is still not implemented but in the list below*

#### To Do List
* Android 12 Splash screen support (using androidx core-splashscreen lib at <https://developer.android.com/jetpack/androidx/releases/core)>
* Base/custom/generic dialog support with:
    * title (visible/gone) - res id or string (see StringIdHelper below)
    * body - res id or string (see StringIdHelper below)
    * positive CTA text/click callback
    * (optional) negative CTA text/click callback
    * cancelOnTouchOutside (Boolean)
    
## LaunchPad Development
Uncomment mavenLocal in main build.gradle.kts to test local library changes.