/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_test

import com.vladmarkovic.sample.common.di.model.AppEntryPointAccessor
import com.vladmarkovic.sample.common.di.model.EntryPoint
import com.vladmarkovic.sample.common.di.model.EntryPointAccessor
import com.vladmarkovic.sample.common.logging.Logger
import com.vladmarkovic.sample.common.logging.LoggerEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext


/**
 * Use Below JUnit5 Extensions to setup in tests:
 * - coroutines
 *
 * Use example:
 *  @ExtendWith(EachTestSetupExtension::class)
 *  internal class SomeTest { ...
 *
 * NOTE: this will run the setup before each test.
 *  To run the setup once for all test class (PREFERABLE), use [AllTestSetupExtension] , or
 *  [CustomizableAllTestSetupExtension] with [setupOnceForAllNested] true (default).
 */
@ExperimentalCoroutinesApi
class EachTestSetupExtension : BeforeEachCallback, AfterEachCallback {
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        resetAll()
    }

    private fun resetAll() {
        Dispatchers.resetMain()
        testDispatcher.scheduler.runCurrent()
    }
}

/**
 * Use example:
 * @ExtendWith(AllTestSetupExtension::class)
 * internal class SomeTest { ...
 *
 * NOTE: For @Nested tests, beforeAll and afterAll are called once for each nested class.
 * To disable this (PREFERABLE), use [CustomizableAllTestSetupExtension] with [setupOnceForAllNested].
 */
@ExperimentalCoroutinesApi
class AllTestSetupExtension : BeforeAllCallback, AfterAllCallback {
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    override fun beforeAll(context: ExtensionContext?) {
        // Because beforeAll/afterAll are called for each @Nested inner test class, we need to first resetAll().
        resetAll()
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterAll(context: ExtensionContext?) {
        resetAll()
    }

    private fun resetAll() {
        Dispatchers.resetMain()
        testDispatcher.scheduler.runCurrent()
    }
}

/**
 * Do not use children of this class with @ExtendWith(TestSetupExtension::class).
 * For full test setup use [EachTestSetupExtension]/[AllTestSetupExtension].
 * Use example:
 * companion object {
 *     @JvmField
 *     @RegisterExtension
 *     val testSetupExtension: TestSetupExtension = TestSetupExtension()
 *         .setupCoroutines()
 *         .doNotRerunSetupForEachNested()
 * }
 * NOTE: For @Nested tests, beforeAll and afterAll are called once for top level class.
 * To disable this - to run setup for each @Nested inner test class use [setupSeparateForEachNested].
 */
@ExperimentalCoroutinesApi
sealed class BaseCustomizableTestSetupExtension(
    open val dispatcher: CoroutineDispatcher? = null,
) : CoroutineScope by TestScope() {

    val testDispatcher: TestDispatcher? get() = dispatcher as? TestDispatcher?

    protected fun setup() {
        dispatcher?.let { Dispatchers.setMain(it) }
    }

    protected fun tearDown() {
        dispatcher?.let {
            testDispatcher?.scheduler?.advanceUntilIdle()
            Dispatchers.resetMain()
        }
    }
}

/** Has to be setup in the companion Object. */
@ExperimentalCoroutinesApi
data class CustomizableAllTestSetupExtension(
    override val dispatcher: CoroutineDispatcher? = null,
    private val setupOnceForAllNested: Boolean = true
) : BaseCustomizableTestSetupExtension(dispatcher), BeforeAllCallback,
    AfterAllCallback {

    private var setupCounter = 0

    override fun beforeAll(context: ExtensionContext?) {
        if (setupOnceForAllNested) {
            setupCounter++
            if (setupCounter > 1) return
        }

        // For the case when beforeAll/afterAll are called for each @Nested inner test class, we need to first resetAll().
        tearDown()

        setup()
    }

    override fun afterAll(context: ExtensionContext?) {
        if (setupOnceForAllNested) {
            setupCounter--
            if (setupCounter > 0) return
        }

        tearDown()
    }
}

/**
 * Can be setup either in the class or in the companion Object.
 */
@ExperimentalCoroutinesApi
data class CustomizableEachTestSetupExtension(
    override val dispatcher: CoroutineDispatcher? = null,
) : BaseCustomizableTestSetupExtension(dispatcher), BeforeEachCallback,
    AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        tearDown()
        setup()
    }

    override fun afterEach(context: ExtensionContext?) {
        tearDown()
    }
}

// region BeforeAllTestSetupExtension builder functions.
@ExperimentalCoroutinesApi
fun CustomizableAllTestSetupExtension.setupCoroutines(dispatcher: CoroutineDispatcher = StandardTestDispatcher())
    : CustomizableAllTestSetupExtension = copy(dispatcher = dispatcher)

@ExperimentalCoroutinesApi
fun CustomizableAllTestSetupExtension.setupOnceForAllNested(): CustomizableAllTestSetupExtension =
    copy(setupOnceForAllNested = true)

@ExperimentalCoroutinesApi
fun CustomizableAllTestSetupExtension.setupSeparateForEachNested(): CustomizableAllTestSetupExtension =
    copy(setupOnceForAllNested = false)

@ExperimentalCoroutinesApi
fun CustomizableAllTestSetupExtension.setupAll(dispatcher: CoroutineDispatcher? = null): CustomizableAllTestSetupExtension =
    copy(dispatcher = dispatcher, setupOnceForAllNested = true)

@ExperimentalCoroutinesApi
fun CustomizableAllTestSetupExtension.setupLogger(): CustomizableAllTestSetupExtension {
    val loggerEntryPoint = object : LoggerEntryPoint {
        override fun logger(): Logger = TestLogger()
    }
    val appEntryPointAccessor = object : AppEntryPointAccessor {
        @Suppress("unchecked_cast")
        override fun <T : EntryPoint> fromApplication(entryPoint: Class<T>): T =
            loggerEntryPoint as T
    }
    EntryPointAccessor.setupWith(appEntryPointAccessor)
    return this
}
// endregion BeforeAllTestSetupExtension builder functions.

// region BeforeEachTestSetupExtension builder functions.
@ExperimentalCoroutinesApi
fun CustomizableEachTestSetupExtension.setupCoroutines(dispatcher: CoroutineDispatcher = StandardTestDispatcher())
    : CustomizableEachTestSetupExtension = copy(dispatcher = dispatcher)

@ExperimentalCoroutinesApi
fun CustomizableEachTestSetupExtension.setupLogger(): CustomizableEachTestSetupExtension {
    setupTestLogger()
    return this
}

fun setupTestLogger() {
    val loggerEntryPoint = object : LoggerEntryPoint {
        override fun logger(): Logger = TestLogger()
    }
    val appEntryPointAccessor = object : AppEntryPointAccessor {
        @Suppress("unchecked_cast")
        override fun <T : EntryPoint> fromApplication(entryPoint: Class<T>): T =
            loggerEntryPoint as T
    }
    EntryPointAccessor.setupWith(appEntryPointAccessor)
}
// endregion BeforeEachTestSetupExtension builder functions.
