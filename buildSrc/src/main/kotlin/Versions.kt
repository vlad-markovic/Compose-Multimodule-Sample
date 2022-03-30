/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

import org.gradle.api.JavaVersion

internal object  Versions {

    // region Global
    val java = JavaVersion.VERSION_11

    const val targetSdk = 31
    const val compileSdk = targetSdk
    const val minSdk = 23
    // endregion Global

    const val gradleBuildTools = "7.1.2"

    // region Kotlin
    const val kotlin = "1.6.10"
    const val ktor = "1.6.8"
    const val kotlinCoroutines = "1.6.0"
    const val kotlinSerialization = "1.3.0"
    // endregion Kotlin

    // region Android / Google
    const val hilt = "2.39.1"
    const val androidXHilt = "1.0.0"
    const val androidXHiltNavCompose = "1.0.0"

    const val googleMaterial = "1.5.0"
    const val appCompat = "1.4.1"
    const val fragmentVersion = "1.4.1"
    const val lifecycle = "2.4.1"
    const val androidxArchCore = "2.1.0"
    const val androidxCoreExtensions = "1.7.0"
    const val fragmentExtensions = "1.4.1"

    const val compose = "1.1.1"
    const val kotlinCompilerExtension = compose
    const val composeActivity = "1.4.0"
    const val googleAccompanist = "0.23.1"
    const val room = "2.4.2"

    const val timber = "5.0.1"
    // endregion Android / Google

    // region Tests
    const val junit5 = "5.8.2"
    const val androidxTest = "1.4.0"
    const val androidxTestJunit = "1.1.3"
    const val kotlinTestRunnerJunit5 = "3.4.2"
    const val mockk = "1.12.3"
    const val slf4jNop = "1.7.32"
    const val coroutinesTesting = "1.6.0"

    const val espresso = "3.4.0"
    // endregion Tests
}
