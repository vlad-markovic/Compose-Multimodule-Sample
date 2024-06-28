/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(Dependencies.gradleBuildTools)
    implementation(Dependencies.kotlin.gradlePlugin)
    implementation(Dependencies.javaPoet)
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-XXLanguage:+TopLevelSealedInheritance",
                "-XXLanguage:+AllowSealedInheritorsInDifferentFilesOfSamePackage",
                "-XXLanguage:+SealedInterfaces",
                "-Xcontext-receivers"
            )
        }
    }
}
