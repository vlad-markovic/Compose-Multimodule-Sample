/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

// For exposing Dependencies for use in buildSrc/build.gradle.kts

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

sourceSets.main {
    java {
        setSrcDirs(setOf(projectDir.parentFile.resolve("src/main/kotlin")))
        include("Versions.kt")
        include("Dependencies.kt")
        include("SimpleCharSequence.kt")
    }
}
