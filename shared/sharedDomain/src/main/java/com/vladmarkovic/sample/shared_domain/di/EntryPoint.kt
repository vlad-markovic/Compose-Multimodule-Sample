/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_domain.di

/**
 * For scoping interfaces annotated with @[dagger.hilt.EntryPoint], for providing injection into objects.
 * If an [EntryPoint] is define in a plain Kotlin module, it cannot be annotated with @[dagger.hilt.EntryPoint]
 * if the implementation which it is providing is in different module which it has no access to.
 * In that case, place an extending interface inside EntryPoints.kt,
 * and annotate that interface with @[dagger.hilt.EntryPoint].
 */
interface EntryPoint
