/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

import org.gradle.api.artifacts.dsl.DependencyHandler

/** [DependencyHandler] extensions for combined [implementation] and similar calls. */

internal fun DependencyHandler.implementationPresentationBase() {
    implementationProject(Project.Core.Kotlin)
    implementationProject(Project.Core.Coroutines)
    implementationProject(Project.Core.Android)
    implementationProject(Project.Common.Logging)
    implementationProject(Project.Common.Di.Model)
    implementationProject(Project.Common.Di.ViewModel)
    implementationProject(Project.Common.Di.Compose)
    implementationProject(Project.Common.Compose)
    implementationProject(Project.Common.Mv.Action)
    implementationProject(Project.Common.Mv.ActionCompose)
    implementationProject(Project.Common.Navigation.Screen.Model)
    implementationProject(Project.Common.Navigation.Screen.NavComponent)
    implementationProject(Project.Common.Navigation.Screen.Compose)
    implementationProject(Project.Common.Navigation.Tab.Model)
    implementationProject(Project.Common.Navigation.Tab.NavComponent)
    implementationProject(Project.Common.Navigation.Tab.Compose)

    // Kotlin
    implementation(Dependencies.kotlin.x.serializationCore)
    implementation(Dependencies.ktor.clientSerialization)
    implementation(Dependencies.kotlin.reflect)

    // Android / Google
    kapt(Dependencies.hilt.androidx.compiler)
    implementation(Dependencies.hilt.androidx.navCompose)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.googleMaterial)
    implementation(Dependencies.androidxArchCoreCommon)
    implementation(Dependencies.androidxCoreExtensions)
    implementation(Dependencies.lifecycleRuntimeExtensions)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.fragmentExtensions)
}

/** Data layer specific dependencies, not inclusive of [implementationPlainKotlinBase] */
fun DependencyHandler.implementationDataBase() {
    implementationProject(Project.Core.Kotlin)
    implementationProject(Project.Common.Logging)
    implementationKtor()
    implementationKotlinCoroutines()
    implementationRoom()
}

fun DependencyHandler.implementationPlainKotlinBase() {
    implementationProject(Project.Core.Kotlin)
    implementation(Dependencies.hilt.dagger.core)
    implementation(Dependencies.kotlin.x.serializationCore)
    implementationKotlinCoroutines()
}

private fun DependencyHandler.implementationKtor() {
    implementation(Dependencies.ktor.clientCore)
    implementation(Dependencies.ktor.clientCio)
    implementation(Dependencies.ktor.clientSerialization)
    implementation(Dependencies.ktor.clientContentNegotiation)
    implementation(Dependencies.ktor.serializationKotlinxJson)
}

fun DependencyHandler.implementationKotlinCoroutines() {
    implementation(Dependencies.kotlin.x.coroutinesCore)
    implementationProject(Project.Core.Coroutines)
}

fun DependencyHandler.implementationRoom() {
    implementation(Dependencies.room.runtime)
    kapt(Dependencies.room.compiler)
    implementation(Dependencies.room.extensions)
}

fun DependencyHandler.implementationCompose() {
    implementation(platform(Dependencies.compose.bom))
    implementation(Dependencies.compose.lifecycle)
    implementation(Dependencies.compose.ui)
    implementation(Dependencies.compose.ui.toolingPreview)
    implementation(Dependencies.compose.material)
    implementation(Dependencies.compose.accompanist.swipeRefresh)
    implementation(Dependencies.compose.accompanist.systemUiController)
    implementation(Dependencies.navigation.runtime)
    implementation(Dependencies.navigation.compose)
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
    testImplementation(Dependencies.kotlin.x.coroutinesCore)
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
    kaptAndroidTest(Dependencies.hilt.dagger.androidCompiler)
    kaptAndroidTest(Dependencies.hilt.androidx.compiler)
    // Espresso
    androidTestImplementation(Dependencies.espressoCore)
    androidTestImplementation(Dependencies.espressoContrib)
    androidTestImplementation(Dependencies.espressoIntents)
    androidTestImplementation(Dependencies.espressoAccessibility)
    androidTestImplementation(Dependencies.espressoIdlingConcurrent)
    androidTestImplementation(Dependencies.espressoIdlingResource)
}
