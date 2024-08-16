/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.common.mv.action"
}

dependencies {
    implementationProject(Project.Core.Coroutines)
    implementation(Dependencies.lifecycleViewModel)
}
