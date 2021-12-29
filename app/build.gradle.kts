plugins {
    id("com.android.application")
    kotlin("android")
    id(Plugins.kotlinKapt)
}

android {
    compileSdk = GlobalVersions.compileSdk

    defaultConfig {
        applicationId = "com.vladmarkovic.sample"
        minSdk = GlobalVersions.minSdk
        targetSdk = GlobalVersions.targetSdk
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }

        multiDexEnabled = true

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }

    kotlinOptions {
        jvmTarget = GlobalVersions.java.toString()
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = GlobalVersions.kotlinCompilerExtension
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = GlobalVersions.java
        targetCompatibility = GlobalVersions.java
    }
}

dependencies {
    // Kotlin
    implementation(Dependencies.kotlinReflect)

    // Android / Google
    implementation(Dependencies.appCompat)
    implementation(Dependencies.googleMaterial)
    implementation(Dependencies.androidxArchCoreCommon)
    implementation(Dependencies.androidxCoreExtensions)
    implementation(Dependencies.lifecycleRuntimeExtensions)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.lifecycleLiveDataExtensions)
    implementation(Dependencies.fragmentExtensions)

    // Compose
    implementation(Dependencies.composeCompiler)
    implementation(Dependencies.composeActivity)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeUiToolingPreview)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeRuntimeRxJava2)
    implementation(Dependencies.composeRuntimeLiveData)

    // Logging
    implementation(Dependencies.timber)

    // Unit tests
    testImplementation(Dependencies.junitJupiterApi)
    testImplementation(Dependencies.junitJupiterEngine)
    testImplementation(Dependencies.junitJupiterParams)
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.kotlinTestJunit5)
    testImplementation(Dependencies.kotlinTestRunnerJunit5)
    testImplementation(Dependencies.mockk)
    testImplementation(Dependencies.slf4jNop)
    testImplementation(Dependencies.archCoreTesting)
    testImplementation(Dependencies.lifecycleRuntimeTesting)

    // Instrumented Tests
    androidTestImplementation(Dependencies.androidxTestCore)
    androidTestImplementation(Dependencies.androidxTestRunner)
    androidTestImplementation(Dependencies.androidxTestRules)
    // Assertions
    androidTestImplementation(Dependencies.androidxTestExtJunit)
    androidTestImplementation(Dependencies.androidxTestExtTruth)
    androidTestImplementation(Dependencies.googleTruth)
    androidTestImplementation(Dependencies.composeUiTest)
    // Needed for createComposeRule, but not createAndroidComposeRule:
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
