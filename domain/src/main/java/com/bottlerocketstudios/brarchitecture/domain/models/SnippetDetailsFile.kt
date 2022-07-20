package com.bottlerocketstudios.brarchitecture.domain.models

data class SnippetDetailsFile(
    val fileName: String?,
    val links: Links?,
    val rawFile: ByteArray? = null
)
