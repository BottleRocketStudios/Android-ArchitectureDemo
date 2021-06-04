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

### Primary Docs
* **Required reading** - [BEST_PRACTICES.md](./docs/BEST_PRACTICES.md) for Android engineering team norms for the project.

### Future functionality
*Note: Evaluate if the implementing something in this list is still best practice prior to coding it.*

*Made up example: If kotlin releases native date/time support (and becomes the go to date time solution for Android), don't implement java 8 date/time libs if it is still not implemented but in the list below*

#### To Do List
* Java 8 Date/Time support (all modules)
* Base/custom/generic dialogfragment support with:
    * title (visible/gone) - res id or string (see StringIdHelper below)
    * body - res id or string (see StringIdHelper below)
    * positive CTA text/click callback
    * (optional) negative CTA text/click callback
    * cancelOnTouchOutside (Boolean)

##### Misc    
```
/**
 * Union type that represents either the int ID or the raw String.
 *
 * Calling getString resolves to a String from either type.
 */
sealed class StringIdHelper : Serializable {
    data class Id(@StringRes val idRes: Int) : StringIdHelper()

    data class Raw(val rawString: String) : StringIdHelper()

    fun getString(context: Context): String {
        return when (this) {
            is Id -> {
                context.getString(idRes)
            }
            is Raw -> {
                rawString
            }
        }
    }
}
```
