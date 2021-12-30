import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project as GradleProject
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*

/** Gradle Project extensions, including LibraryExtension extensions. */

/** Common plugins and dependencies for each presentation module. */
fun GradleProject.configurePresentationModule(includeSharedPresentation: Boolean = true) {
    configureAndroidModule()

    configureComposeInModule()

    dependencies {
        if (includeSharedPresentation) implementation(project(Project.sharedPresentation))

        implementationPresentationBase()

        configureUnitTests()
        testImplementation(project(Project.sharedTest))
        androidTestImplementationAll()
    }

    androidLibrary {
        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        packagingOptions {
            resources.excludes.addAll(excludePackagingOptions)
        }
    }
}

/** Common for data and domain modules. */
fun GradleProject.configureAndroidModule() {
    applyPlugin(Plugins.androidLibrary)
    applyPlugin(Plugins.kotlinKapt)
    applyPlugin(Plugins.kotlinAndroid)
    applyPlugin(Plugins.hilt)

    version = "1.0"

    configureJavaPluginExtension()

    androidLibrary {
        configureAndroidSdkVersions()

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
        targetSdk = Versions.targetSdk
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
    "/META-INF/{AL2.0,LGPL2.1}"
)
