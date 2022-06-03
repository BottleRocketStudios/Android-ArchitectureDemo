package com.bottlerocketstudios.brarchitecture.ui

import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryBrowserData
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryFileData

@Suppress("FunctionName")
object Routes {
    const val Main = "main"
    const val Home = "home"
    const val Splash = "splash"
    const val AuthCode = "authcode"
    const val DevOptions = "devoptions"
    const val Snippets = "snippets"
    const val CreateSnippet = "snippets/create"
    const val Profile = "profile"

    // Example path with arguments; will remove after first real path with arguments is in place.
    fun UserProfile(id: String) = "profile/{$id}"

    fun RepositoryBrowser(data: RepositoryBrowserData)  =
        "repository?repoName=${data.repoName}" +
            (data.folderHash?.let { hash ->
                "&folderHash=$hash"
            } ?: "") +
            (data.folderPath?.let { path ->
                "&folderPath=$path"
            } ?: "")

    fun RepositoryFile(data: RepositoryFileData) =
        "file?hash=${data.hash}&path=${data.path}" +
            if (data.mimeType.isNotEmpty()) "&mimeType=${data.mimeType}" else ""
}
