/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()
configureComposeInModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.common.navigation.screen.compose"
}

dependencies {
    implementationProject(Project.Core.Kotlin)
    implementationProject(Project.Core.Coroutines)
    implementationProject(Project.Common.Logging)
    implementationProject(Project.Common.Di.Model)
    implementationProject(Project.Common.Di.Compose)
    implementationProject(Project.Common.Mv.Action)
    implementationProject(Project.Common.Navigation.Screen.Model)
    implementationProject(Project.Common.Navigation.Screen.NavComponent)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.hilt.androidx.navCompose)
}
