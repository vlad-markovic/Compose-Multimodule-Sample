/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.common.logger"
    dependencies {
        implementationProject(Project.Common.Di)
        implementationProject(Project.Common.Logging)
        implementation(Dependencies.timber)
    }
}
