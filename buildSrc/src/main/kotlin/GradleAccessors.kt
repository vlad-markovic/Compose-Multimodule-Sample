/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

import com.android.build.gradle.LibraryExtension
import org.gradle.kotlin.dsl.*
import org.gradle.api.Plugin
import org.gradle.api.Project as GradleProject
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.dependencies

/**
 * Extensions for accessing standard extensions
 * in non gradle.build.kts files, where Gradle Kotlin DSL is not available.
 */

// region DependencyHandler extensions
fun DependencyHandler.implementation(dependencyNotation: Any) {
    add("implementation", dependencyNotation)
}

fun DependencyHandler.debugImplementation(dependencyNotation: Any) {
    add("debugImplementation", dependencyNotation)
}

fun DependencyHandler.testImplementation(dependencyNotation: Any) {
    add("testImplementation", dependencyNotation)
}

fun DependencyHandler.androidTestImplementation(dependencyNotation: Any) {
    add("androidTestImplementation", dependencyNotation)
}

fun DependencyHandler.kapt(dependencyNotation: Any) {
    add("kapt", dependencyNotation)
}
// endregion DependencyHandler extensions

// region Gradle Project extensions
/** Applies a Gradle plugin with [id], if it has not been applied before. */
fun GradleProject.applyPlugin(id: String): Plugin<Any> =
    (plugins.findPlugin(id) ?: plugins.apply(id))

/** Use as android block where not accessible in Gradle utils */
fun GradleProject.androidLibrary(action: LibraryExtension.() -> Unit) {
    (extensions.getByName("android") as LibraryExtension).action()
}

/**
 * Use for [Project.Layered] and single module projects to implement by specifying the [Project],
 *  and not implementation(project(String)) using String. Ie:
 *   implementationProject(Project.Feature.Main) // not [Project.Layered]
 *   implementationProject(Project.Feature.Home) // is [Project.Layered]
 *  instead of:
 *   implementationProject(Project.Feature.Main.presentation)
 *   implementationProject(Project.Feature.Home.data)
 *   implementationProject(Project.Feature.Home.domain)
 *   implementationProject(Project.Feature.Home.presentation)
 *
 * If you need just to implement just one of the modules from the [Project.Layered], than use:
 *   implementationProject(Project.Feature.Home.domain) // is from [Project.Layered]
 */
fun GradleProject.implementationProject(project: Project, configuration: String? = null) {
    dependencies {
        project.modules.forEach { module ->
            implementation(project(module, configuration))
        }
    }
}

fun GradleProject.testImplementationProject(project: Project, configuration: String? = null) {
    dependencies {
        project.modules.forEach { module ->
            testImplementation(project(module, configuration))
        }
    }
}
// endregion Gradle Project extensions
