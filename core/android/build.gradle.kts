/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.core.android"
}

dependencies {
    implementation(Dependencies.lifecycleRuntimeExtensions)
    implementationProject(Project.Core.Coroutines)
}
