/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.common.di.viewmodel"
}

dependencies {
    implementationProject(Project.Common.Di.Model)
}
