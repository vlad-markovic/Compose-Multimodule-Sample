/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.common.navigation.screen.navcomponent"
}

dependencies {
    implementationProject(Project.Core.Android)
    implementationProject(Project.Common.Logging)
    implementationProject(Project.Common.Navigation.Screen.Model)
    implementationProject(Project.Common.Mv.Action)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.hilt.androidx.navCompose)
}
