package com.bottlerocketstudios.brarchitecture.domain.models

/**
 * Two versions of this class.
 * SnippetDetailsFile is returned when requesting a specific file
 * SnippetDetailsFileLinks is returned when requesting a snippet
 */
data class SnippetDetailsFile(
    val fileName: String,
    val rawFile: ByteArray,
): DomainModel

data class SnippetDetailsFileLinks(
    val fileName: String,
    val links: Links,
): DomainModel
