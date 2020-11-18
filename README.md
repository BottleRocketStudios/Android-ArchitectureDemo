Bottle Rocket Android Architecture Guidelines
=============================================

## How to Open Project in Android Studio
* Clone project to local machine
* Open project in Android Studio
* If prompted, select "Default Gradle Wrapper"
* Project should open and Gradle should sync without any issues. 
* Generate a Bitbucket OAuth consumer key at https://bitbucket.org/chriskoeberle/workspace/settings/api
* Create a file in project root named "apikey.properties" with following format:
BITBUCKET_KEY="<key>"
BITBUCKET_SECRET="<secret>"

### Primary Docs
* **Required reading** - [BEST_PRACTICES.md](./docs/BEST_PRACTICES.md) for Android engineering team norms for the project.

### Future functionality
*Note: Evaluate if the implementing something in this list is still best practice prior to coding it.*

*Made up example: If kotlin releases native date/time support (and becomes the go to date time solution for Android), don't implement java 8 date/time libs if it is still not implemented but in the list below*

#### To Do List
* Retrofit coroutine support with Repository functions returning a result type (wrapping the original type)
    * Swap out retrofit `fun foo(): Call<Foo>` with coroutine support like `suspend fun foo(): Response<Bar>`
    * ApiResult class creation (the result type). Example code below (reach out to JS for more up to date code)
    * Modify all repository function signatures that interact with the retrofit service to be `suspend fun` AND wrapping the return type `Foo` in ApiResult (like `ApiResult<Foo>`)
    * Unwrap the `ApiResult<Foo>` at a higher level (likely Android `ViewModel`) with a when statement (forced as an expression)/expression to handle success/failure (with UI implications) appropriately per call/screen
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

##### Example ApiResult code
```
/**
 * Wrapper class for [Success] type that also represent various [Failure]s with appropriate associated data.
 *
 * Note that a loading type at this level is unnecessary. Loading status can be handled at a higher level if needed but hasn't been very useful from past experience.
 */
sealed class ApiResult<out SUCCESS_TYPE : Any> {

    /** Indicates a successful operation (ex: successful api response) */
    data class Success<out SUCCESS_TYPE : Any>(val data: SUCCESS_TYPE) : ApiResult<SUCCESS_TYPE>()

    /** Indicates a failed operation (ex: api response error, parsing error, exception thrown, etc) */
    sealed class Failure : ApiResult<Nothing>() {
        /** Network failures due to network connection issues (no connection, airplane mode, wifi connected with no internet) */
        object Network : Failure()

        /** Retrofit+Moshi parse failures or other errors related to the api calls */
        data class Server(val message: String, val errorCode: Int?) : Failure()

        /** General failure type bucket */
        data class GeneralFailure(val message: String) : Failure()
    }
}

/**
 * Executes the [transform] block only when receiver [ApiResult.Success] to modify the wrapped value, similar to Collection/List map function.
 *
 * Note that you need to wrap the value you return back from the [transform] block (at the call site) into an [ApiResult].
 * This gives you the ability to change the type from [ApiResult.Success] to [ApiResult.Failure] based on business logic/etc
 * Ex: Mapping a Dto to DomainModel where the mapping cannot be performed due to invalid data, etc should likely return a [ApiResult.Failure]
 *
 * Note that failure has not been thoroughly tested yet but _should_ work.
 */
fun <T : Any, R : Any> ApiResult<T>.map(transform: (T) -> ApiResult<R>): ApiResult<R> {
    return when (this) {
        is ApiResult.Success -> transform(data)
        is ApiResult.Failure -> this
    }
}

/** Syntax sugar to wrap the receiver [T] into [ApiResult.Success] and support chaining */
fun <T : Any> T.asSuccess(): ApiResult.Success<T> = ApiResult.Success(this)

/** Syntax sugar to convert the receiver [Exception] into [ApiResult.Failure.GeneralFailure] */
fun Exception.asGeneralFailure(): ApiResult.Failure.GeneralFailure = ApiResult.Failure.GeneralFailure(localizedMessage.orEmpty())

/** Helper function to convert from a [Response] to the appropriate [ApiResult] */
internal fun <T : Any> Response<T>.toResult(): ApiResult<T> {
    return try {
        when {
            this.isSuccessful -> {
                val body = body()
                if (body != null) {
                    ApiResult.Success(body)
                } else {
                    Timber.w("[toResult] Response body null")
                    ApiResult.Failure.GeneralFailure("null response body")
                }
            }
            else -> {
                Timber.w("[toResult] Api not successful: message ${this.message()} code: ${this.code()}")
                ApiResult.Failure.Server(this.message(), this.code())
            }
        }
    } catch (e: Exception) {
        Timber.w(e, "[toResult] Unknown login api error {$e.localizedMessage} stackTrace: {$e.stackTrace}")
        ApiResult.Failure.GeneralFailure(e.localizedMessage ?: "")
    }
}

/** Helper function to convert from a [Response] to the appropriate [ApiResult], returning [ApiResult.Success] ([Unit]) on success */
internal fun <T : Any> Response<T>.toEmptyResult(): ApiResult<Unit> {
    return try {
        when {
            this.isSuccessful -> ApiResult.Success(Unit)
            else -> {
                Timber.w("[toEmptyResult] Api not successful: message ${this.message()} code: ${this.code()}")
                ApiResult.Failure.Server(this.message(), this.code())
            }
        }
    } catch (e: Exception) {
        Timber.w(e, "[toEmptyResult] Unknown login api error {$e.localizedMessage} stackTrace: {$e.stackTrace}")
        ApiResult.Failure.GeneralFailure(e.localizedMessage ?: "")
    }
}

/**
 * Executes [block], converting any caught exceptions to [ApiResult.Failure.GeneralFailure] with logging
 *
 * @param className Name of calling class (for logging purposes)
 * @param methodName Name of calling function (for logging purposes)
 * @param block Lambda with logic that returns the appropriate [ApiResult] response
 *
 */
internal suspend fun <T : Any> wrapExceptions(className: String, methodName: String, block: suspend () -> ApiResult<T>): ApiResult<T> {
    return try {
        Timber.tag(className).v("[$methodName]")
        block().also { Timber.tag(className).v("[$methodName] result=$it") }
    } catch (e: Exception) {
        e.asGeneralFailure().also { Timber.tag(className).w(e, "[$methodName wrapExceptions] exception caught and converted to failure: $it") }
    }
}

```
