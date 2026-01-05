@file:Suppress("FunctionNaming")

package com.bottlerocketstudios.brarchitecture.ui

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavKey {
        @Serializable sealed interface TopLevel : NavKey

        @Serializable sealed interface Shared : NavKey
}

object Routes {
        @Serializable object Main : NavKey
        @Serializable object Home : NavKey, NavKey.TopLevel
        @Serializable object Splash : NavKey
        @Serializable object AuthCode : NavKey
        @Serializable object DevOptions : NavKey
        @Serializable object Snippets : NavKey, NavKey.TopLevel
        @Serializable object Profile : NavKey, NavKey.TopLevel
        @Serializable object PullRequests : NavKey, NavKey.TopLevel
        @Serializable object Commits : NavKey
        @Serializable object Branches : NavKey
        @Serializable object Projects : NavKey, NavKey.TopLevel
        @Serializable object FeatureToggles : NavKey

        @Serializable
        data class RepositoryBrowser(
                val repoName: String = "",
                val folderHash: String? = null,
                val folderPath: String? = null
        ) : NavKey

        @Serializable
        data class RepositoryFile(val hash: String, val path: String, val mimeType: String = "") :
                NavKey
}
