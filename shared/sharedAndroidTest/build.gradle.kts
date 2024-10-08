/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()
configureJavaPluginExtension()

configureComposeInModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.shared_android_test"
}

dependencies {
    implementationProject(Project.Common.Mv.Action)
    implementationProject(Project.Common.Mv.ActionCompose)
    implementationProject(Project.Common.Navigation.Screen.Model)
    implementationProject(Project.Common.Navigation.Tab.Model)
    implementationProject(Project.Common.Navigation.Tab.NavComponent)
    implementationProject(Project.Shared.Layers.domain)
    implementationProject(Project.Shared.Layers.presentation)

    implementation(Dependencies.junitJupiterApi)
    implementation(Dependencies.androidxArchCoreCommon)
    implementation(Dependencies.archCoreTesting)
    implementation(Dependencies.kotlinTest)
    implementation(Dependencies.coroutinesTesting)
    implementation(Dependencies.hilt.androidx.navCompose)
}
