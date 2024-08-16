/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()
configureComposeInModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.common.navigation.tab.compose"
}

dependencies {
    implementationProject(Project.Core.Coroutines)
    implementationProject(Project.Common.Di.ViewModel)
    implementationProject(Project.Common.Di.Compose)
    implementationProject(Project.Common.Mv.Action)
    implementationProject(Project.Common.Navigation.Screen.Model)
    implementationProject(Project.Common.Navigation.Screen.NavComponent)
    implementationProject(Project.Common.Navigation.Screen.Compose)
    implementationProject(Project.Common.Navigation.Tab.Model)
    implementationProject(Project.Common.Navigation.Tab.NavComponent)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.hilt.androidx.navCompose)
}
