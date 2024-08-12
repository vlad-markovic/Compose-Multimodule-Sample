/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

configureAndroidModule()
configureComposeInModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.common.compose"
}

dependencies {
    implementationKotlinCoroutines()
}
