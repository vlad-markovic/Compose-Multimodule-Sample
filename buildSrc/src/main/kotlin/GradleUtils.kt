/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project as GradleProject
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*

/** Gradle Project extensions, including LibraryExtension extensions. */

/** Common plugins and dependencies for each domain module. */
fun GradleProject.configureDomainModule(includeSharedDomain: Boolean = true) {
    configurePlainKotlinModule()

    dependencies {
        if (includeSharedDomain) {
            implementationProject(Project.Shared.domain)
        }

        configureUnitTests()
    }
}

/** Common plugins and dependencies for any plain kotlin (domain) module. */
private fun GradleProject.configurePlainKotlinModule() {
    applyPlugin(Plugins.kotlinSerialization)
    applyPlugin(Plugins.javaLibrary)
    applyPlugin(Plugins.kotlin)

    configureJavaPluginExtension()

    dependencies {
        implementationPlainKotlinBase()
    }
}

/** Common plugins and dependencies for each data module. */
fun GradleProject.configureDataModule(includeSharedData: Boolean = true) {
    configureAndroidModule()

    dependencies {
        if (includeSharedData) {
            implementationProject(Project.Shared.data)
        }
        implementationProject(Project.Shared.domain)
        testImplementationProject(Project.SharedTest)

        implementationDataBase()

        configureUnitTests()
    }
}

/** Common plugins and dependencies for each presentation module. */
fun GradleProject.configurePresentationModule(includeSharedPresentation: Boolean = true) {
    configureAndroidModule()
    configureComposeInModule()

    dependencies {
        if (includeSharedPresentation) {
            implementationProject(Project.Shared.presentation)
        }
        implementationProject(Project.Shared.domain)
        testImplementationProject(Project.SharedTest)
        androidTestImplementationProject(Project.SharedTest)
        androidTestImplementationProject(Project.SharedAndroidTest)

        implementationPresentationBase()

        configureUnitTests()
        androidTestImplementationAll()
    }

    androidLibrary {
        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        packaging {
            resources.excludes.addAll(excludePackagingOptions)
        }
    }
}

/** Common for data and domain modules. */
fun GradleProject.configureAndroidModule() {
    applyPlugin(Plugins.androidLibrary)
    applyPlugin(Plugins.kotlinAndroid)
    applyPlugin(Plugins.kotlinKapt)
    applyPlugin(Plugins.kotlinSerialization)
    applyPlugin(Plugins.hilt)

    version = "1.0"

    configureJavaPluginExtension()

    androidLibrary {
        configureAndroidSdkVersions()

        defaultConfig {
            namespace = "com.vladmarkovic.sample"
        }

        dependencies {
            implementation(Dependencies.androidxCoreExtensions)
            implementation(Dependencies.hiltAndroid)
            kapt(Dependencies.hiltAndroidCompiler)
        }
    }
}

fun LibraryExtension.configureAndroidSdkVersions() {
    compileSdk = Versions.compileSdk
    defaultConfig {
        minSdk = Versions.minSdk
        testOptions.targetSdk = Versions.targetSdk
    }
}

fun GradleProject.configureJavaPluginExtension() {
    configure<JavaPluginExtension> {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }
}

fun GradleProject.configureComposeInModule() {
    enableCompose()
    dependencies { implementationCompose() }
}

fun GradleProject.enableCompose() {
    androidLibrary {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = Versions.kotlinCompilerExtension
        }
    }
}

fun GradleProject.configureUnitTests() {
    tasks.withType(Test::class.java).all {
        useJUnitPlatform()
    }
    dependencies {
        testImplementationAll()
    }
}

val excludePackagingOptions = listOf(
    "/META-INF/{AL2.0,LGPL2.1}",
    "META-INF/proguard/androidx-annotations.pro",
    "META-INF/gradle/incremental.annotation.processors",
    "META-INF/DEPENDENCIES",
    "META-INF/LICENSE",
    "META-INF/LICENSE.txt",
    "META-INF/license.txt",
    "META-INF/LICENSE-notice.txt",
    "META-INF/license-notice.txt",
    "META-INF/LICENSE.md",
    "META-INF/license.md",
    "META-INF/LICENSE-notice.md",
    "META-INF/license-notice.md",
    "META-INF/NOTICE",
    "META-INF/NOTICE.txt",
    "META-INF/notice.txt",
    "META-INF/ASL2.0",
    "META-INF/{AL2.0,LGPL2.1}",
    "META-INF/*.kotlin_module"
)
