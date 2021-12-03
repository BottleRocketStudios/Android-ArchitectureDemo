/**
 * Renames app bundle to value specified in BuildInfoManager.createAabFilename(...) when `BuildInfoManager.shouldOverrideApkAndAabNames()` is true
 * Expected to be included in non-library android modules (likely just :app) via: `apply(from = "../renameAppBundle.gradle.kts")`
 * Inspiration from https://stackoverflow.com/a/69305535/201939 and https://stackoverflow.com/a/54010142/201939
 */
if (BuildInfoManager.shouldOverrideApkAndAabNames()) {
    tasks.whenTaskAdded {
        val verboseLogging = false
        // Skip some unnecessary tasks
        if (name.startsWith("bundle") &&
            !name.contains("Classes") &&
            !name.contains("Resources") &&
            name != "bundle"
        ) {
            val deletePreviouslyRenamedAabTaskName = "deletePreviouslyRenamed${name.capitalize()}Aab"
            val renameAabTaskName = "rename${name.capitalize()}Aab"
            val deleteOriginalAabTaskName = "deleteOriginal${name.capitalize()}Aab"

            // Quick and dirty way to derive variant name from the current bundleVARIANT task name.
            val variant = name.substring("bundle".length).decapitalize()
            // Quick and dirty way to derive the buildType and flavor from the variant
            val indexOfFlavor = variant.indexOfFirst { it.isUpperCase() }.takeIf { it != -1 }
            val flavor = indexOfFlavor?.let { variant.substring(0, it) }
            val buildType = indexOfFlavor?.let { variant.substring(it).decapitalize() }

            val aabFolderPath = "$buildDir/outputs/bundle/$variant/"
            val originalAabFilename = "app-$flavor-$buildType.aab"
            val updatedAabFilename = BuildInfoManager.createAabFilename(variant)

            if (flavor != null && buildType != null && variant.isNotBlank()) {
                if (verboseLogging) {
                    println("[renameAab] flavor=$flavor")
                    println("[renameAab] buildType=$buildType")
                    println("[renameAab] variant=$variant")
                    println("[renameAab] aabFolderPath=$aabFolderPath")
                    println("[renameAab] originalAabFilename=$originalAabFilename")
                    println("[renameAab] updatedAabFilename=$updatedAabFilename")
                }

                // Cleans up any existing aab file with matching name to allow the future copy-rename task to succeed
                tasks.register<Delete>(deletePreviouslyRenamedAabTaskName) {
                    delete("$aabFolderPath/$updatedAabFilename")
                    doLast {
                        println("[deletePreviouslyRenamedAab] delete previously renamed bundle task completed - updatedAabFilename=$updatedAabFilename")
                    }
                }

                // Copy-renames the aab file from the original name to the new name
                tasks.register<Copy>(renameAabTaskName) {
                    dependsOn(deletePreviouslyRenamedAabTaskName)
                    from(aabFolderPath)
                    setDestinationDir(file(aabFolderPath))
                    rename(originalAabFilename, updatedAabFilename)
                    doLast {
                        println("[renameAab] rename bundle task completed - updatedAabFilename$updatedAabFilename")
                    }
                }

                // Cleans up the original aab file to prevent multiple aabs being present in the folder
                tasks.register<Delete>(deleteOriginalAabTaskName) {
                    dependsOn(renameAabTaskName)

                    delete("$aabFolderPath/$originalAabFilename")
                    doLast {
                        println("[deleteOriginalAab] delete original bundle task completed - originalAabFilename=$originalAabFilename")
                    }
                }

                finalizedBy(deletePreviouslyRenamedAabTaskName, renameAabTaskName, deleteOriginalAabTaskName)
            }
        }
    }
}
