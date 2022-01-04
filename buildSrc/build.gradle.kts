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
    implementation(Dependencies.kotlinGradlePlugin)
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-XXLanguage:+TopLevelSealedInheritance",
                "-XXLanguage:+AllowSealedInheritorsInDifferentFilesOfSamePackage",
                "-XXLanguage:+SealedInterfaces"
            )
        }
    }
}
