/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

applyPlugin(Plugins.android.library)
applyPlugin(Plugins.kotlin.android)

configureJavaVersion()

androidLibrary {
    namespace = "com.vladmarkovic.sample.common.android"

    configureAndroidSdkVersions()
}

dependencies {
    implementation(Dependencies.androidxCoreExtensions)
}
