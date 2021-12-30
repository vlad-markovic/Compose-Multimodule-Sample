import org.gradle.api.artifacts.dsl.DependencyHandler

/** [DependencyHandler] extensions for combined [implementation] and similar calls. */

internal fun DependencyHandler.implementationPresentationBase() {
    // Kotlin
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

    // Logging
    implementation(Dependencies.timber)
}

/** Data layer specific dependencies, not inclusive of [implementationPlainKotlinBase] */
fun DependencyHandler.implementationDataBase() {
    implementationKtor()
    implementationKotlinCoroutines()
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

fun DependencyHandler.implementationCompose() {
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
    testImplementation(Dependencies.coroutinesTesting)
    // removes error: Failed to load class "org.slf4j.impl.StaticLoggerBinder"
    testImplementation(Dependencies.slf4jNop)
    testImplementation(Dependencies.hiltAndroidTesting)
}

fun DependencyHandler.androidTestImplementationAll() {
    androidTestImplementation(Dependencies.androidxTestCore)
    androidTestImplementation(Dependencies.androidxTestRunner)
    androidTestImplementation(Dependencies.androidxTestRules)
    // Assertions
    androidTestImplementation(Dependencies.androidxTestExtJunit)
    androidTestImplementation(Dependencies.androidxTestExtTruth)
    androidTestImplementation(Dependencies.googleTruth)
    androidTestImplementation(Dependencies.composeUiTest)
    // Needed for createComposeRule
    debugImplementation(Dependencies.composeUiTestManifest)
    debugImplementation(Dependencies.composeUiTooling)
    // Espresso
    androidTestImplementation(Dependencies.espressoCore)
    androidTestImplementation(Dependencies.espressoContrib)
    androidTestImplementation(Dependencies.espressoIntents)
    androidTestImplementation(Dependencies.espressoAccessibility)
    androidTestImplementation(Dependencies.espressoIdlingConcurrent)
    androidTestImplementation(Dependencies.espressoIdlingResource)
}
