configureAndroidModule()
configureJavaPluginExtension()

dependencies {
    implementationProject(Project.Shared.domain)
    implementation(Dependencies.junitJupiterApi)
    implementation(Dependencies.androidxArchCoreCommon)
    implementation(Dependencies.archCoreTesting)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.kotlinTest)
    implementation(Dependencies.coroutinesTesting)
}
