/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()
configureJavaPluginExtension()

configureComposeInModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.shared_android_test"
}

dependencies {
    implementationProject(Project.Shared.domain)
    implementationProject(Project.Shared.presentation)

    implementation(Dependencies.junitJupiterApi)
    implementation(Dependencies.androidxArchCoreCommon)
    implementation(Dependencies.archCoreTesting)
    implementation(Dependencies.kotlinTest)
    implementation(Dependencies.coroutinesTesting)
    implementation(Dependencies.hilt.androidx.navCompose)
}
