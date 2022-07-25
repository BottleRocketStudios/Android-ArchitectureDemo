package com.bottlerocketstudios.brarchitecture.ui.repository

import java.io.Serializable

@Suppress("SerialVersionUIDInSerializableClass")
data class RepositoryBrowserData(
    val repoName: String,
    val folderHash: String? = null,
    val folderPath: String? = null,
) : Serializable
