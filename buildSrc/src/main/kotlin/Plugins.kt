/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

@Suppress("ConstPropertyName")
object Plugins {

    val kotlin = Kotlin
    val android = Android

    const val javaLibrary = "java-library"
    const val hilt = "com.google.dagger.hilt.android"

    object Kotlin : SimpleCharSequence("kotlin") {
        const val kapt = "kotlin-kapt"
        const val serialization = "org.jetbrains.kotlin.plugin.serialization"
        const val android = "org.jetbrains.kotlin.android"
        const val composeCompiler = "org.jetbrains.kotlin.plugin.compose"
    }

    object Android {
        const val application = "com.android.application"
        const val library =  "com.android.library"
    }
}
