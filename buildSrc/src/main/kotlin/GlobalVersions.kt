/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

/** Versions which also need access outside of buildSrc - in app module. */
object GlobalVersions {
    val java = Versions.java

    const val targetSdk = Versions.targetSdk
    const val compileSdk = Versions.compileSdk
    const val minSdk = Versions.minSdk

    const val kotlinCompilerExtension = Versions.kotlinCompilerExtension
}
