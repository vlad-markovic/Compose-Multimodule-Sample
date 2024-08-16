/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

plugins {
    id(Plugins.android.application)
    id(Plugins.kotlin.android)
    id(Plugins.kotlin.kapt)
    id(Plugins.hilt)
    id(Plugins.kotlin.composeCompiler)
}

android {
    compileSdk = GlobalVersions.compileSdk

    defaultConfig {
        applicationId = "com.vladmarkovic.sample"
        namespace = applicationId

        minSdk = GlobalVersions.minSdk
        targetSdk = GlobalVersions.targetSdk
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }

        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }

    compileOptions {
        sourceCompatibility = GlobalVersions.java
        targetCompatibility = GlobalVersions.java
    }

    kotlinOptions {
        jvmTarget = GlobalVersions.java.toString()
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    packaging {
        resources.excludes.addAll(excludePackagingOptions)
    }

    lint {
        ignoreWarnings = false
        warningsAsErrors = true
        abortOnError = true
    }
}

dependencies {
    // Projects
    implementationProject(Project.Shared)
    implementationProject(Project.Main)
    implementationProject(Project.Feature.Settings)
    implementationProject(Project.Feature.Post)
    implementationProject(Project.Feature.Covid)
    implementationProject(Project.Common.Di.Model)
    implementationProject(Project.Common.Logging)
    implementationProject(Project.Common.Logging.Android)
    implementationProject(Project.Common.Mv.Action)
    implementationProject(Project.Common.Navigation.Screen.Model)

    // Kotlin
    implementation(Dependencies.kotlin.reflect)

    // Injection
    implementation(Dependencies.hilt.dagger.android)
    kapt(Dependencies.hilt.dagger.androidCompiler)
    kapt(Dependencies.hilt.androidx.compiler)

    // Android / Google
    implementation(Dependencies.appCompat)
    implementation(Dependencies.googleMaterial)
    implementation(platform(Dependencies.compose.bom))
    implementation(Dependencies.compose.runtime)

    // Logging
    implementation(Dependencies.timber)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

// Reduces incremental compilation times by reducing how often
// an incremental change causes a rebuild of the Dagger components.
hilt {
    enableAggregatingTask = true
}
