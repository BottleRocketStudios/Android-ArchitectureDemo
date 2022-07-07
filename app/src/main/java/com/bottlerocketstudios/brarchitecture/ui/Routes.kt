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
    const val CreateSnippet = "snippets/create"
    const val Profile = "profile"

    fun repositoryBrowser(data: RepositoryBrowserData) =
        "repository?repoName=${data.repoName}" +
            (if (data.folderHash.orEmpty().isNotEmpty()) "&folderHash=${data.folderHash}" else "") +
            (if (data.folderPath.orEmpty().isNotEmpty()) "&folderPath=${data.folderPath}" else "")

    fun repositoryFile(data: RepositoryFileData) =
        "file?hash=${data.hash}&path=${data.path}" +
            if (data.mimeType.isNotEmpty()) "&mimeType=${data.mimeType}" else ""
}
