pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "Sample"

include(":app")
include(":shared:sharedPresentation")
include(":shared:sharedTest")
include(":main:mainPresentation")
include(":features:feed:feedPresentation")
