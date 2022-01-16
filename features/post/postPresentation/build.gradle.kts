/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

configurePresentationModule()

androidLibrary {
    sourceSets {
        getByName("test").java.srcDirs(
            "src/test/java",
            "src/sharedTest/java",
        )
        getByName("androidTest").java.srcDirs(
            "src/androidTest/java",
            "src/sharedTest/java",
        )
    }
    tasks {
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-Xopt-in=kotlin.RequiresOptIn"
                )
            }
        }
    }
}

dependencies {
    implementationProject(Project.Feature.Post.domain)
    androidTestImplementationProject(Project.Feature.Post.data)
    androidTestImplementationProject(Project.Main)
}
