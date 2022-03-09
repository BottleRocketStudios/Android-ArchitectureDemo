package com.bottlerocketstudios.brarchitecture.ui.repository

import java.io.Serializable

data class RepositoryFileData(
    val hash: String,
    val path: String,
    val mimeType: String
) : Serializable
