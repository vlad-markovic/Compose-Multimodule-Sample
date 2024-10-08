/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                }
            )
        }
        expectSuccess = true
//        HttpResponseValidator {
//            handleResponseExceptionWithRequest { exception, request ->
//                val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
//                val exceptionResponse = clientException.response
//                if (exceptionResponse.status == HttpStatusCode.NotFound) {
//                    val exceptionResponseText = exceptionResponse.bodyAsText()
//                    throw clientException
//                    // TODO throw CustomException(exceptionResponse, exceptionResponseText)
//                }
//            }
//        }
    }
}
