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
