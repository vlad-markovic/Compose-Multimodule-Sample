/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

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
