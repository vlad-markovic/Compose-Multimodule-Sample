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
        implementationProject(Project.Common.Logging)

        if (includeSharedDomain) {
            implementationProject(Project.Shared.Layers.domain)
        }

        configureUnitTests()
    }
}

/** Common plugins and dependencies for any plain kotlin (domain) module. */
fun GradleProject.configurePlainKotlinModule() {
    applyPlugin(Plugins.kotlin.serialization)
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
            implementationProject(Project.Shared.Layers.data)
        }
        implementationProject(Project.Shared.Layers.domain)
        testImplementationProject(Project.Shared.Test)

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
            implementationProject(Project.Shared.Layers.presentation)
        }
        implementationProject(Project.Shared.Layers.domain)
        testImplementationProject(Project.Shared.Test)
        androidTestImplementationProject(Project.Shared.Test)
        androidTestImplementationProject(Project.Shared.AndroidTest)

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
    applyPlugin(Plugins.android.library)
    applyPlugin(Plugins.kotlin.android)
    applyPlugin(Plugins.kotlin.kapt)
    applyPlugin(Plugins.kotlin.serialization)
    applyPlugin(Plugins.hilt)

    version = "1.0"

    configureJavaVersion()

    androidLibrary {
        configureAndroidSdkVersions()

        dependencies {
            implementationProject(Project.Common.Android)
            implementation(Dependencies.androidxCoreExtensions)
            implementation(Dependencies.hilt.dagger.android)
            kapt(Dependencies.hilt.dagger.androidCompiler)
        }

        lint {
            ignoreWarnings = false
            warningsAsErrors = true
            abortOnError = true
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

fun GradleProject.configureJavaVersion() {
    configureJavaPluginExtension()
    androidLibrary {
        compileOptions {
            sourceCompatibility = Versions.java
            targetCompatibility = Versions.java
        }
    }
}

fun GradleProject.configureComposeInModule() {
    applyPlugin(Plugins.kotlin.composeCompiler)
    enableCompose()
    dependencies { implementationCompose() }
}

fun GradleProject.enableCompose() {
    androidLibrary {
        buildFeatures {
            compose = true
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
