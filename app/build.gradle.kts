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
        resources.excludes.addAll(excludePackagingOptions)
    }

    compileOptions {
        sourceCompatibility = GlobalVersions.java
        targetCompatibility = GlobalVersions.java
    }
}

dependencies {
    // Projects
    implementation(project(Project.sharedPresentation))
    implementation(project(Project.mainPresentation))

    // Kotlin
    implementation(Dependencies.kotlinReflect)

    // Android / Google
    implementation(Dependencies.appCompat)
    implementation(Dependencies.googleMaterial)

    // Logging
    implementation(Dependencies.timber)
}
