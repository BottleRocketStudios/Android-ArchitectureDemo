package com.bottlerocketstudios.brarchitecture.data.environment

// FIXME: Adjust environment list/naming to match actual client environments.
/** Represents available client environments */
enum class EnvironmentType(val shortName: String) {
    /** Primary dev/QA environment used today. */
    STG("STG"),
    /** Production environment */
    PRODUCTION("PRODUCTION")
}
