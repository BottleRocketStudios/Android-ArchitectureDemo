Bottle Rocket Android Architecture Guidelines
=============================================

## How to Open Project in Android Studio
* Clone project to local machine
* Open project in Android Studio
* If prompted, select "Default Gradle Wrapper"
* Project should open and Gradle should sync without any issues.

## OAuth consumer key creation and setup steps
* Generate a Bitbucket OAuth consumer key at https://bitbucket.org/[your-bitbucket-username]/workspace/settings/api
    * You must specify a callback url. The value isn't important, so go ahead and use https://www.bottlerocketstudios.com/
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
* **Required reading** - [NEW_PROJECT_STARTER.md](./docs/NEW_PROJECT_STARTER.md) for information on how to use this repository as a new project starter.
* **Required reading** - [BEST_PRACTICES.md](./docs/BEST_PRACTICES.md) for Android engineering team norms for the project.

## Pre-PR Creation Checklist
1. Create 1+ commits locally until work is completed.
2. Execute the Android Studio `ci - internalDebug (minus clean) - dev build` Run Configuration (via `Run` -> `Run...` or `Ctrl+Opt+R` on macOS)
    * Fix any failures that surface due to linting, compilation, or unit tests.
3. Build/install the `internalDebugMini` variant on your testing emulator/device (enables proguard and will crash if proguard rules need tweaking but weren't)
4. Smoke test the app and ensure no regressions/crashes/unexpected behavior.
5. Create PR!

## Future functionality
*Note: Evaluate if the implementing something in this list is still best practice prior to coding it.*

*Made up example: If kotlin releases native date/time support (and becomes the go to date time solution for Android), don't implement java 8 date/time libs if it is still not implemented but in the list below*

#### To Do List
* Android 12 Splash screen support (using androidx core-splashscreen lib at https://developer.android.com/jetpack/androidx/releases/core)
* Rewrite DevOptions screen in Compose
* Base/custom/generic dialogfragment support with:
    * title (visible/gone) - res id or string (see StringIdHelper below)
    * body - res id or string (see StringIdHelper below)
    * positive CTA text/click callback
    * (optional) negative CTA text/click callback
    * cancelOnTouchOutside (Boolean)