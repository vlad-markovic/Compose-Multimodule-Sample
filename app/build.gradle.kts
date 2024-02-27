/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

plugins {
    id("com.android.application")
    kotlin("android")
    id(Plugins.kotlinKapt)
    id(Plugins.hilt)
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

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
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
    composeOptions {
        kotlinCompilerExtensionVersion = GlobalVersions.kotlinCompilerExtension
    }

    packaging {
        resources.excludes.addAll(excludePackagingOptions)
    }
}

dependencies {
    // Projects
    implementationProject(Project.Shared)
    implementationProject(Project.Main)
    implementationProject(Project.Feature.Settings)
    implementationProject(Project.Feature.Post)
    implementationProject(Project.Feature.Covid)

    // Kotlin
    implementation(Dependencies.kotlinReflect)

    // Injection
    implementation(Dependencies.hiltAndroid)
    kapt(Dependencies.hiltAndroidCompiler)
    kapt(Dependencies.androidXHiltCompiler)

    // Android / Google
    implementation(Dependencies.appCompat)
    implementation(Dependencies.googleMaterial)
    implementation(Dependencies.composeActivity)

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
