/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.common.navigation.tab.navcomponent"
}

dependencies {
    implementationProject(Project.Common.Di.Model)
    implementationProject(Project.Common.Di.ViewModel)
    implementationProject(Project.Common.Mv.Action)
    implementationProject(Project.Common.Navigation.Screen.Model)
    implementationProject(Project.Common.Navigation.Tab.Model)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.hilt.androidx.navCompose)
}
