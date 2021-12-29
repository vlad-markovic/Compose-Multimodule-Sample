import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

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
// endregion DependencyHandler extensions

// region Gradle Project extensions
/** Applies a Gradle plugin with [id], if it has not been applied before. */
fun Project.applyPlugin(id: String): Plugin<Any> =
    (plugins.findPlugin(id) ?: plugins.apply(id))

/** Use as android block where not accessible in Gradle utils */
fun Project.androidLibrary(action: LibraryExtension.() -> Unit) {
    (extensions.getByName("android") as LibraryExtension).action()
}
// endregion Gradle Project extensions
