pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "Sample"

val maxDirLevelToTraverse = 3

val excludeDirs = arrayOf(
    rootDir.name,
    "buildSrc",
    "build",
    "gradle",
    ".gradle",
    ".idea",
    ".git",
    ".navigation",
    "bin",
    "gen",
    "src",
    "captures"
)

/** I.e for home-domain inside features/home: features:home:home-domain, or :androidApp top level. */
val File.project: String get() =
    ":" + absolutePath.substring(absolutePath.indexOf(rootDir.name) + rootDir.name.length + 1)
        .replace('/', ':')

fun File.shouldTraverse(currentlyWalkedDirAbsolutePath: String): Boolean = isInclusiveDir()
        // walk starts from the dir it self, so prevent duplicate processing of mid level dirs.
        && currentlyWalkedDirAbsolutePath != this.absolutePath

fun File.isModuleToInclude(): Boolean = isInclusiveDir() && hasBuildGradleFile()

fun File.isInclusiveDir(): Boolean = isDirectory && name !in excludeDirs

fun File.hasBuildGradleFile(): Boolean = file("$absolutePath/build.gradle.kts").exists()

/**
 * Traverse dir (not sub-dirs) and continue to sub-dir recursively only if [shouldTraverse],
 * making sure that sub-dirs of excluded ones are not traversed too,
 * and include module dirs which [isModuleToInclude].
 */
fun File.traverseAndIncludeModules(level: Int = 1) {
    if (level > maxDirLevelToTraverse) return

    walk()
        .maxDepth(1)
        .forEach {
            if (it.isModuleToInclude()) {
                include(it.project)
            } else if (it.shouldTraverse(absolutePath)) {
                it.traverseAndIncludeModules(level + 1)
            }
        }
}

/**
 * Include all modules identifying them by presence of build.gradle.kts (except root and buildSrc)
 * and detecting the dir they are in from root.
 */
rootDir.traverseAndIncludeModules()
