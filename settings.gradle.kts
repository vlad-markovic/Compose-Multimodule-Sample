pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "Sample"

include(":app")
include(":shared:sharedData")
include(":shared:sharedDomain")
include(":shared:sharedPresentation")
include(":shared:sharedTest")
include(":main:mainPresentation")
include(":features:post:postData")
include(":features:post:postDomain")
include(":features:post:postPresentation")
