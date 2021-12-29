object Dependencies {

    // region Kotlin
    val kotlinGradlePlugin: String = kotlinDependency("kotlin-gradle-plugin")
    val kotlinReflect = kotlinDependency("kotlin-reflect")

    private fun jetbrainsKotlin(module: String, version: String? = null): String =
        dependency("org.jetbrains.kotlin", module, version)
    // endregion Kotlin

    // region Compose
    val composeCompiler = dependency("androidx.compose.compiler", "compiler", Versions.compose)
    val composeActivity = dependency("androidx.activity", "activity-compose", Versions.composeActivity)
    val composeUi = composeUi("ui")
    val composeUiToolingPreview = composeUi("ui-tooling-preview")
    val composeMaterial = dependency("androidx.compose.material", "material", Versions.compose)
    val composeRuntimeRxJava2 = dependency("androidx.compose.runtime", "runtime-rxjava2", Versions.compose)
    val composeRuntimeLiveData = dependency("androidx.compose.runtime", "runtime-livedata", Versions.compose)

    private fun composeUi(module: String, version: String = Versions.compose) =
        dependency("androidx.compose.ui", module, version)
    // region Compose

    // region Android
    val gradleBuildTools: String = dependency("com.android.tools.build", "gradle", Versions.gradleBuildTools)
    val appCompat = dependency("androidx.appcompat", "appcompat", Versions.appCompat)
    val googleMaterial = dependency("com.google.android.material", "material", Versions.googleMaterial)
    val androidxArchCoreCommon = dependency("androidx.arch.core", "core-common", Versions.androidxArchCore)
    val androidxCoreExtensions = dependency("androidx.core", "core-ktx", Versions.androidxCoreExtensions)
    val lifecycleViewModel = lifecycle("lifecycle-viewmodel-ktx")
    val lifecycleRuntimeExtensions = lifecycle("lifecycle-runtime-ktx")
    val lifecycleLiveDataExtensions = lifecycle("lifecycle-livedata-ktx")
    val fragmentExtensions = dependency("androidx.fragment", "fragment-ktx", Versions.fragmentExtensions)

    private fun lifecycle(module: String, version: String = Versions.lifecycle) =
        dependency("androidx.lifecycle", module, version)

    val timber: String = dependency("com.jakewharton.timber", "timber", Versions.timber)
    // endregion Android

    // region Test
    // Unit Tests
    val junitJupiterApi =  junitJupiter("junit-jupiter-api")
    val junitJupiterEngine =  junitJupiter("junit-jupiter-engine")
    val junitJupiterParams =  junitJupiter("junit-jupiter-params")
    val kotlinTest =  jetbrainsKotlin("kotlin-test")
    val kotlinTestJunit5 =  jetbrainsKotlin("kotlin-test-junit5", Versions.kotlin)
    val kotlinTestRunnerJunit5 =  dependency("io.kotlintest","kotlintest-runner-junit5", Versions.kotlinTestRunnerJunit5)
    val mockk =  dependency("io.mockk","mockk", Versions.mockk)
    // removes error: Failed to load class "org.slf4j.impl.StaticLoggerBinder"
    val slf4jNop =  dependency("org.slf4j","slf4j-nop", Versions.slf4jNop)
    val archCoreTesting =  dependency("androidx.arch.core", "core-testing", Versions.androidxArchCore)
    val lifecycleRuntimeTesting = dependency("androidx.lifecycle","lifecycle-runtime-testing", Versions.lifecycle)

    // UI Tests
    val androidxTestCore = androidxTest("core")
    val androidxTestRunner = androidxTest("runner")
    val androidxTestRules = androidxTest("rules")
    val androidxTestExtJunit = androidxTestExt("junit", Versions.androidxTestJunit)
    val androidxTestExtTruth = androidxTestExt("truth")
    val googleTruth = dependency("com.google.truth","truth", Versions.googleTruth)
    val composeUiTest = composeUi("ui-test-junit4")
    // Needed for createComposeRule
    val composeUiTestManifest = composeUi("ui-test-manifest")
    val composeUiTooling = composeUi("ui-tooling")
    // Espresso
    val espressoCore = androidxTestEspresso("espresso-core")
    val espressoContrib = androidxTestEspresso("espresso-contrib")
    val espressoIntents = androidxTestEspresso("espresso-intents")
    val espressoAccessibility = androidxTestEspresso("espresso-accessibility")
    val espressoIdlingConcurrent = dependency("androidx.test.espresso.idling","idling-concurrent", Versions.espresso)
    val espressoIdlingResource = androidxTestEspresso("espresso-idling-resource")
    // endregion test

    private fun dependency(group: String, module: String, version: String? = null): String =
        "$group:$module" + version?.let { ":$it" }.orEmpty()

    private fun kotlinDependency(module: String, version: String? = Versions.kotlin): String =
        dependency("org.jetbrains.kotlin", module, version)

    private fun androidxTest(module: String, version: String? = Versions.androidxTest): String =
        dependency("androidx.test", module, version)

    private fun androidxTestExt(module: String, version: String? = Versions.androidxTest): String =
        dependency("androidx.test.ext", module, version)

    private fun androidxTestEspresso(module: String, version: String? = Versions.espresso): String =
        dependency("androidx.test.espresso", module, version)

    private fun junitJupiter(module: String, version: String? = Versions.junit5): String =
        dependency("org.junit.jupiter", module, version)
}
