/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.common.viewaction"
}

dependencies {
    implementationProject(Project.Core.Coroutines)
    implementation(Dependencies.lifecycleViewModel)
}
