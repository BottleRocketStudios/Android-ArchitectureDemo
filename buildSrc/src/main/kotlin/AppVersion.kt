/**
 * Represents the app version. Provides [versionCode] to generate the versionCode used as well as [versionName] to generate the versionName.
 *
 * Note the restrictions on [major]/[minor]/[patch]/[hotfix]
 *
 * See https://confluence.bottlerocketapps.com/display/BKB/Android+Coding+Standards#AndroidCodingStandards-VersionCodes
 */
data class AppVersion(
    /** Must be >= 0 and < 100 */
    val major: Int,
    /** Must be >= 0 and < 100 */
    val minor: Int,
    /** Must be >= 0 and < 100 */
    val patch: Int,
    /** Must be >= 0 and < 10 */
    val hotfix: Int,
    /** If true, [versionName] can look like `1.0.0`, If false, [versionName] will look like `1.0`. Usually project/client dependant */
    val showEmptyPatchNumberInVersionName: Boolean
) {

    init {
        check(major in 0 until 100) { "major version number must be >= 0 and < 100" }
        check(minor in 0 until 100) { "minor version number must be >= 0 and < 100" }
        check(patch in 0 until 100) { "patch version number must be >= 0 and < 100" }
        check(hotfix in 0 until 10) { "hotfix version number must be >= 0 and < 10" }
    }

    fun logString(): String {
        return "$this, versionCode=$versionCode, versionName=$versionName"
    }

    /**
     * Returns value to use for android.versionCode
     *
     * Ex:
     * *  `100000`
     * * `1001030`
     * *  `204000`
     * *  `500001`
     */
    val versionCode: Int
        get() = (major * 100000) + (minor * 1000) + (patch * 10) + hotfix

    /**
     * Returns value to use for android.versionName. Shows [patch] of 0 only when [showEmptyPatchNumberInVersionName] is true.
     *
     * EX:
     * * `1.0.0` (or `1.0`)
     * * `10.1.3`
     * * `2.4.0` (or `2.4`)
     * * `5.0.0` (or `5.0`)
     */
    val versionName: String
        get() {
            return if (!showEmptyPatchNumberInVersionName && patch == 0) {
                "$major.$minor"
            } else {
                "$major.$minor.$patch"
            }
        }
}
