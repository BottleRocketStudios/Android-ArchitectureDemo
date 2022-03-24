package com.bottlerocketstudios.brarchitecture.domain.models

// FIXME: Adjust environment list/naming to match actual client environments.
/** Represents available client environments */
enum class EnvironmentType(val shortName: String) : DomainModel {
    /** Primary dev/QA environment used today. */
    STG("STG"),

    /** Production environment */
    PRODUCTION("PRODUCTION"),
}
