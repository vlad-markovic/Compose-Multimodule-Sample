/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

configurePresentationModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.post_presentation"
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
            compilerOptions {
                freeCompilerArgs.addAll(
                    "-Xopt-in=kotlin.RequiresOptIn"
                )
            }
        }
    }
}

dependencies {
    implementationProject(Project.Common.Mv.State)
    implementationProject(Project.Common.Mv.Mvi)
    implementationProject(Project.Feature.Post.domain)
    androidTestImplementationProject(Project.Feature.Post.data)
    androidTestImplementationProject(Project.Main)
}
