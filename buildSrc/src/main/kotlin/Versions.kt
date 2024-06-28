/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

import org.gradle.api.JavaVersion

@Suppress("ConstPropertyName")
internal object  Versions {

    // region Global
    val java = JavaVersion.VERSION_17

    const val targetSdk = 34
    const val compileSdk = targetSdk
    const val minSdk = 24
    // endregion Global

    const val gradleBuildTools = "8.4.0"

    // region Kotlin
    const val kotlin = "2.0.0"
    const val ktor = "2.3.12"
    const val kotlinCoroutines = "1.9.0-RC"
    const val kotlinSerialization = "1.6.3"
    // endregion Kotlin

    // region Android / Google
    const val dagger = "2.50"
    const val hilt = "1.2.0"

    const val googleMaterial = "1.11.0"
    const val appCompat = "1.6.1"
    const val fragmentVersion = "1.6.2"
    const val lifecycle = "2.7.0"
    const val androidxArchCore = "2.1.0"
    const val androidxCoreExtensions = "1.12.0"

    const val composeBom = "2024.06.00"
    const val googleAccompanist = "0.34.0"
    const val room = "2.6.1"

    const val timber = "5.0.1"
    // endregion Android / Google

    // region Tests
    const val junit5 = "5.10.2"
    const val androidxTest = "1.5.0"
    const val androidxTestExtJunit = "1.1.5"
    const val kotlinTestRunnerJunit5 = "3.4.2"
    const val mockk = "1.13.9"
    const val slf4jNop = "2.0.12"
    const val coroutinesTesting = "1.8.0"

    const val espresso = "3.5.1"
    // endregion Tests
}
