import org.gradle.api.JavaVersion

internal object Versions {

    // region Global
    val java = JavaVersion.VERSION_11

    const val targetSdk = 31
    const val compileSdk = targetSdk
    const val minSdk = 23
    // endregion Global

    const val gradleBuildTools = "7.0.2"

    // region Kotlin
    const val kotlin = "1.5.31"
    // endregion Kotlin

    // region Android
    const val googleMaterial = "1.4.0"
    const val appCompat = "1.4.0"
    const val lifecycle = "2.4.0"
    const val androidxArchCore = "2.1.0"
    const val androidxCoreExtensions = "1.7.0"
    const val fragmentExtensions = "1.4.0"

    const val compose = "1.0.5"
    const val kotlinCompilerExtension = compose
    const val composeActivity = "1.4.0"

    const val timber = "5.0.1"
    // endregion Android

    // region Tests
    const val junit5 = "5.8.2"
    const val androidxTest = "1.4.0"
    const val androidxTestJunit = "1.1.3"
    const val googleTruth = "1.1.3"
    const val kotlinTestRunnerJunit5 = "3.4.2"
    const val mockk = "1.12.1"
    const val slf4jNop = "1.7.32"

    const val espresso = "3.4.0"
    // endregion Tests
}
