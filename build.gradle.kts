/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.kotlinGradlePlugin)
        classpath(Dependencies.gradleBuildTools)
        classpath(Dependencies.hiltPlugin)
        classpath(Dependencies.kotlinSerialization)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
