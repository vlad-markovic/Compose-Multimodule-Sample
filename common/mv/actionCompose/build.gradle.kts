/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()
configureComposeInModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.common.viewaction"
}

dependencies {
    implementationProject(Project.Core.Coroutines)
    implementationProject(Project.Core.Android)
    implementationProject(Project.Common.Di.Model)
    implementationProject(Project.Common.Di.ViewModel)
    implementationProject(Project.Common.Di.Compose)
    implementationProject(Project.Common.Mv.Action)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.hilt.androidx.navCompose)
}
