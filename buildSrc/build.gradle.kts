plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(Dependencies.gradleBuildTools)
    implementation(Dependencies.kotlinGradlePlugin)
}
