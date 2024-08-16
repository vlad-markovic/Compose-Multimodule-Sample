/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.common.mv.mvi"
}


dependencies {
    implementationProject(Project.Common.Mv.Action)
    implementationProject(Project.Common.Mv.State)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.hilt.dagger.core)
}
