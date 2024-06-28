/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

/** Versions which also need access outside of buildSrc - in app module. */
@Suppress("MayBeConstant")
object GlobalVersions {
    val java = Versions.java
    val kotlin = Versions.kotlin
    val dagger = Versions.dagger

    const val targetSdk = Versions.targetSdk
    const val compileSdk = Versions.compileSdk
    const val minSdk = Versions.minSdk
}
