configureAndroidModule()
configureJavaPluginExtension()

dependencies {
    implementation(project(Project.sharedDomain))
    implementation(Dependencies.junitJupiterApi)
    implementation(Dependencies.androidxArchCoreCommon)
    implementation(Dependencies.archCoreTesting)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.kotlinTest)
    implementation(Dependencies.coroutinesTesting)
}
