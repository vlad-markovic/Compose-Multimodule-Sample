/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()
configureJavaPluginExtension()

androidLibrary {
    namespace = "com.vladmarkovic.sample.shared_test"
}

dependencies {
    implementationProject(Project.Common.Di.Model)
    implementationProject(Project.Common.Logging)
    implementationProject(Project.Shared.domain)
    implementation(Dependencies.junitJupiterApi)
    implementation(Dependencies.androidxArchCoreCommon)
    implementation(Dependencies.archCoreTesting)
    implementation(Dependencies.kotlinTest)
    implementation(Dependencies.coroutinesTesting)
}
