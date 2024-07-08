/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

object Dependencies {

    val kotlin = Kotlin
    val hilt = Hilt
    val ktor = Ktor
    val room = Room
    val compose = Compose
    val navigation = Navigation

    object Kotlin {
        val gradlePlugin: String = kotlinDependency("kotlin-gradle-plugin")
        val reflect = kotlinDependency("kotlin-reflect")
        val serialization = kotlinDependency("kotlin-serialization")

        val x = X
        object X {
            val coroutinesCore: String = jetbrainsKotlinx("kotlinx-coroutines-core")
            val serializationCore: String = jetbrainsKotlinx("kotlinx-serialization-core", Versions.kotlinSerialization)

            private fun jetbrainsKotlinx(module: String, version: String = Versions.kotlinCoroutines): String =
                dependency("org.jetbrains.kotlinx", module, version)
        }

        private fun kotlinDependency(module: String, version: String? = Versions.kotlin): String =
            dependency("org.jetbrains.kotlin", module, version)
    }


    private fun jetbrainsKotlin(module: String, version: String? = null): String =
        dependency("org.jetbrains.kotlin", module, version)

    object Ktor {
        val clientCore = ktor("ktor-client-core")
        val clientCio = ktor("ktor-client-cio")
        val clientSerialization = ktor("ktor-client-serialization")
        val clientContentNegotiation = ktor("ktor-client-content-negotiation")
        val serializationKotlinxJson = ktor("ktor-serialization-kotlinx-json")

        private fun ktor(module: String, version: String = Versions.ktor): String =
            dependency("io.ktor", module, version)
    }

    object Hilt {
        val dagger = Dagger
        val androidx = AndroidX

        object Dagger {
            val core = dagger("hilt-core")
            //    val hiltPlugin = hilt("hilt-android-gradle-plugin")
            val android = dagger("hilt-android")
            val androidCompiler = dagger("hilt-android-compiler")
        }

        object AndroidX {
            val compiler = androidxHilt("hilt-compiler", Versions.hilt)
            val navCompose = androidxHilt("hilt-navigation-compose", Versions.hilt)

            private fun androidxHilt(module: String, version: String) =
                dependency("androidx.hilt", module, version)
        }
    }

    private fun dagger(module: String, version: String = Versions.dagger) =
        dependency("com.google.dagger", module, version)

    object Compose {
        val bom = dependency("androidx.compose", "compose-bom", Versions.composeBom)
        //    val composeCompiler = dependency("androidx.compose.compiler", "compiler", Versions.compose)
        //    val activity = dependency("androidx.activity", "activity-compose")
        val runtime = dependency("androidx.compose.runtime", "runtime")
        val lifecycle = dependency("androidx.lifecycle", "lifecycle-runtime-compose")
        val material = dependency("androidx.compose.material", "material")
        val ui = UI
        val accompanist = Accompanist

        object UI : SimpleCharSequence(composeUi("ui")) {
            val toolingPreview = composeUi("ui-tooling-preview")
        }
        fun composeUi(module: String) = dependency("androidx.compose.ui", module)

        object Accompanist {
            val swipeRefresh = googleAccompanist("accompanist-swiperefresh")
            val systemUiController = googleAccompanist("accompanist-systemuicontroller")

            private fun googleAccompanist(module: String, version: String = Versions.googleAccompanist) =
                dependency("com.google.accompanist", module, version)
        }
    }

    object Navigation {
        val runtime = dependency("androidx.navigation", "navigation-runtime-ktx", Versions.navigation)
    }

    object Room {
        val runtime = room("room-runtime")
        val compiler = room("room-compiler")
        val extensions = room("room-ktx")

        private fun room(module: String, version: String = Versions.room) =
            dependency("androidx.room", module, version)
    }

    // region Android
    val gradleBuildTools: String = dependency("com.android.tools.build", "gradle", Versions.gradleBuildTools)
    val appCompat = dependency("androidx.appcompat", "appcompat", Versions.appCompat)
    val googleMaterial = dependency("com.google.android.material", "material", Versions.googleMaterial)
    val androidxArchCoreCommon = dependency("androidx.arch.core", "core-common", Versions.androidxArchCore)
    val androidxCoreExtensions = dependency("androidx.core", "core-ktx", Versions.androidxCoreExtensions)
    val lifecycleViewModel = lifecycle("lifecycle-viewmodel-ktx")
    val lifecycleRuntimeExtensions = lifecycle("lifecycle-runtime-ktx")
    val fragmentExtensions = dependency("androidx.fragment", "fragment-ktx", Versions.fragmentVersion)

    private fun lifecycle(module: String, version: String = Versions.lifecycle) =
        dependency("androidx.lifecycle", module, version)

    val timber: String = dependency("com.jakewharton.timber", "timber", Versions.timber)
    // endregion Android

    val javaPoet: String = dependency("com.squareup", "javapoet", "1.13.0")

    // region Test
    // Unit Tests
    val junitJupiterApi = junitJupiter("junit-jupiter-api")
    val junitJupiterEngine = junitJupiter("junit-jupiter-engine")
    val junitJupiterParams = junitJupiter("junit-jupiter-params")
    val kotlinTest = jetbrainsKotlin("kotlin-test")
    val kotlinTestJunit5 = jetbrainsKotlin("kotlin-test-junit5", Versions.kotlin)
    val kotlinTestRunnerJunit5 = dependency("io.kotlintest","kotlintest-runner-junit5", Versions.kotlinTestRunnerJunit5)
    val mockk = dependency("io.mockk","mockk", Versions.mockk)
    val mockkAndroid = dependency("io.mockk","mockk-android", Versions.mockk)
    // removes error: Failed to load class "org.slf4j.impl.StaticLoggerBinder"
    val slf4jNop = dependency("org.slf4j","slf4j-nop", Versions.slf4jNop)
    val archCoreTesting = dependency("androidx.arch.core", "core-testing", Versions.androidxArchCore)
    val lifecycleRuntimeTesting = lifecycle("lifecycle-runtime-testing", "2.3.0-alpha01")
    val coroutinesTesting = dependency("org.jetbrains.kotlinx","kotlinx-coroutines-test", Versions.coroutinesTesting)
    val hiltAndroidTesting = dagger("hilt-android-testing")

    // UI Tests
    val androidxTestCore = androidxTest("core")
    val androidxTestRunner = androidxTest("runner")
    val androidxTestRules = androidxTest("rules")
    val androidxTestExtJunit = androidxTestExt("junit", Versions.androidxTestExtJunit)
    val androidxTestExtTruth = androidxTestExt("truth")
    val composeUiTest = Compose.composeUi("ui-test-junit4")
    // Needed for createComposeRule
    val composeUiTestManifest = Compose.composeUi("ui-test-manifest")
    val composeUiTooling = Compose.composeUi("ui-tooling")
    val fragmentTesting = dependency("androidx.fragment","fragment-testing", Versions.fragmentVersion)
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

    private fun androidxTest(module: String, version: String? = Versions.androidxTest): String =
        dependency("androidx.test", module, version)

    private fun androidxTestExt(module: String, version: String? = Versions.androidxTest): String =
        dependency("androidx.test.ext", module, version)

    private fun androidxTestEspresso(module: String, version: String? = Versions.espresso): String =
        dependency("androidx.test.espresso", module, version)

    private fun junitJupiter(module: String, version: String? = Versions.junit5): String =
        dependency("org.junit.jupiter", module, version)
}
