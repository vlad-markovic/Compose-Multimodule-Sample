/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.kotlin.android") apply false
//    id("com.google.devtools.ksp") apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
//    id("dagger.hilt.android.plugin") apply false
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            url = java.net.URI("https://oss.sonatype.org/content/repositories/snapshots")
            content {
                includeModule("com.google.dagger", "hilt-android-gradle-plugin")
            }
        }
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
