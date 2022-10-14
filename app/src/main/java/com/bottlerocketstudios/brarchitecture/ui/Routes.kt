@file:Suppress("FunctionNaming")
package com.bottlerocketstudios.brarchitecture.ui

import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserData
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileData

object Routes {
    const val Main = "main"
    const val Home = "home"
    const val Splash = "splash"
    const val AuthCode = "authcode"
    const val DevOptions = "devoptions"
    const val Snippets = "snippets"
    const val Profile = "profile"
    const val PullRequests = "pullRequests"
    const val Commits = "commits"
    const val Branches = "branches"

    fun RepositoryBrowser(data: RepositoryBrowserData = RepositoryBrowserData("{repoName}", "{folderHash}", "{folderPath}")) =
        "repository?repoName=${data.repoName}" +
            (if (data.folderHash.orEmpty().isNotEmpty()) "&folderHash=${data.folderHash}" else "") +
            (if (data.folderPath.orEmpty().isNotEmpty()) "&folderPath=${data.folderPath}" else "")

    fun RepositoryFile(data: RepositoryFileData = RepositoryFileData("{hash}", "{path}", "{mimeType}")) =
        "file?hash=${data.hash}&path=${data.path}" +
            if (data.mimeType.isNotEmpty()) "&mimeType=${data.mimeType}" else ""
}
