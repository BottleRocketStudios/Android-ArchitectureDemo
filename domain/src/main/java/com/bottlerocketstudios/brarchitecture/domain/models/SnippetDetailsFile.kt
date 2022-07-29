package com.bottlerocketstudios.brarchitecture.domain.models

data class SnippetDetailsFile(
    val fileName: String? = null,
    val links: Links? = null,
    val rawFile: ByteArray? = null,
)
