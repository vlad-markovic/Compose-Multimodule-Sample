/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

plugins {
    id(Plugins.android.application) apply false
    id(Plugins.android.library) apply false
    id(Plugins.kotlin.android) apply false
    id(Plugins.hilt) version GlobalVersions.dagger apply false
    id(Plugins.kotlin.serialization) version GlobalVersions.kotlin apply false
    id(Plugins.kotlin.composeCompiler) version GlobalVersions.kotlin apply false
//    id("com.google.devtools.ksp") apply false
}

tasks.register("clean", Delete::class) {
    rootProject.allprojects.forEach {
        delete(it.layout.buildDirectory)
    }
}
