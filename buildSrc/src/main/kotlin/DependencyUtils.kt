/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

import org.gradle.api.artifacts.dsl.DependencyHandler

/** [DependencyHandler] extensions for combined [implementation] and similar calls. */

internal fun DependencyHandler.implementationPresentationBase() {
    // Kotlin
    implementation(Dependencies.kotlinxSerializationCore)
    implementation(Dependencies.ktorClientSerialization)
    implementation(Dependencies.kotlinReflect)

    // Android / Google
    kapt(Dependencies.androidXHiltCompiler)
    implementation(Dependencies.hiltNavCompose)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.googleMaterial)
    implementation(Dependencies.androidxArchCoreCommon)
    implementation(Dependencies.androidxCoreExtensions)
    implementation(Dependencies.lifecycleRuntimeExtensions)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.lifecycleLiveDataExtensions)
    implementation(Dependencies.fragmentExtensions)
}

/** Data layer specific dependencies, not inclusive of [implementationPlainKotlinBase] */
fun DependencyHandler.implementationDataBase() {
    implementationKtor()
    implementationKotlinCoroutines()
    implementationRoom()
}

fun DependencyHandler.implementationPlainKotlinBase() {
    implementation(Dependencies.hiltCore)
    implementation(Dependencies.kotlinxSerializationCore)
    implementationKotlinCoroutines()
}

private fun DependencyHandler.implementationKtor() {
    implementation(Dependencies.ktorClientCore)
    implementation(Dependencies.ktorClientCio)
    implementation(Dependencies.ktorClientSerialization)
}

fun DependencyHandler.implementationKotlinCoroutines() {
    implementation(Dependencies.kotlinxCoroutinesCore)
}

fun DependencyHandler.implementationRoom() {
    implementation(Dependencies.roomRuntime)
    kapt(Dependencies.roomCompiler)
    implementation(Dependencies.roomExtensions)
}

fun DependencyHandler.implementationCompose() {
    implementation(Dependencies.composeBom)
    implementation(Dependencies.composeCompiler)
    implementation(Dependencies.composeActivity)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeUiToolingPreview)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeRuntimeLiveData)
    implementation(Dependencies.composeSwipeRefresh)
    implementation(Dependencies.composeSystemUiController)
}

fun DependencyHandler.testImplementationAll() {
    testImplementation(Dependencies.junitJupiterApi)
    testImplementation(Dependencies.junitJupiterEngine)
    testImplementation(Dependencies.junitJupiterParams)
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.kotlinTestJunit5)
    testImplementation(Dependencies.kotlinTestRunnerJunit5)
    testImplementation(Dependencies.mockk)
    testImplementation(Dependencies.archCoreTesting)
    testImplementation(Dependencies.lifecycleRuntimeTesting)
    testImplementation(Dependencies.kotlinxCoroutinesCore)
    testImplementation(Dependencies.coroutinesTesting)
    // removes error: Failed to load class "org.slf4j.impl.StaticLoggerBinder"
    testImplementation(Dependencies.slf4jNop)
}

fun DependencyHandler.androidTestImplementationAll() {
    androidTestImplementation(Dependencies.mockkAndroid)
    androidTestImplementation(Dependencies.androidxTestCore)
    androidTestImplementation(Dependencies.androidxTestRunner)
    androidTestImplementation(Dependencies.androidxTestRules)
    // Assertions
    androidTestImplementation(Dependencies.androidxTestExtJunit)
    androidTestImplementation(Dependencies.androidxTestExtTruth)
    androidTestImplementation(Dependencies.composeUiTest)
    androidTestImplementation(Dependencies.hiltAndroidTesting)
    // Needed for createComposeRule
    debugImplementation(Dependencies.composeUiTestManifest)
    debugImplementation(Dependencies.composeUiTooling)
    debugImplementation(Dependencies.fragmentTesting)
    kaptAndroidTest(Dependencies.hiltAndroidCompiler)
    kaptAndroidTest(Dependencies.androidXHiltCompiler)
    // Espresso
    androidTestImplementation(Dependencies.espressoCore)
    androidTestImplementation(Dependencies.espressoContrib)
    androidTestImplementation(Dependencies.espressoIntents)
    androidTestImplementation(Dependencies.espressoAccessibility)
    androidTestImplementation(Dependencies.espressoIdlingConcurrent)
    androidTestImplementation(Dependencies.espressoIdlingResource)
}
