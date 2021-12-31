package com.vladmarkovic.sample.shared_test

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.*

// In order to test LiveData, the `InstantTaskExecutorRule` rule needs to be applied via JUnit.
// As we are running it with Spek, the "rule" will be implemented in this way instead.
@SuppressLint("RestrictedApi")
fun instantTaskExecutorRuleStart() =
    ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
        override fun executeOnDiskIO(runnable: Runnable) {
            runnable.run()
        }

        override fun isMainThread(): Boolean {
            return true
        }

        override fun postToMainThread(runnable: Runnable) {
            runnable.run()
        }
    })

@SuppressLint("RestrictedApi")
fun instantTaskExecutorRuleFinish() = ArchTaskExecutor.getInstance().setDelegate(null)

/**
 * Use Below JUnit5 Extensions to setup in tests:
 * - coroutines
 * - LiveData
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
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    override fun beforeEach(context: ExtensionContext?) {
        instantTaskExecutorRuleStart()
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        resetAll()
    }

    private fun resetAll() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        instantTaskExecutorRuleFinish()
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
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    override fun beforeAll(context: ExtensionContext?) {
        // Because beforeAll/afterAll are called for each @Nested inner test class, we need to first resetAll().
        resetAll()
        Dispatchers.setMain(testDispatcher)
        instantTaskExecutorRuleStart()
    }

    override fun afterAll(context: ExtensionContext?) {
        resetAll()
    }

    private fun resetAll() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        instantTaskExecutorRuleFinish()
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
 *         .setupLiveData()
 *         .setupCoroutines()
 *         .doNotRerunSetupForEachNested()
 * }
 * NOTE: For @Nested tests, beforeAll and afterAll are called once for top level class.
 * To disable this - to run setup for each @Nested inner test class use [setupSeparateForEachNested].
 */
@ExperimentalCoroutinesApi
sealed class BaseCustomizableTestSetupExtension(
    open val dispatcher: CoroutineDispatcher? = null,
    private val setupLiveData: Boolean = false
) : TestCoroutineScope by TestCoroutineScope() {

    val testDispatcher: TestCoroutineDispatcher? get() = dispatcher as? TestCoroutineDispatcher?

    protected fun setup() {
        dispatcher?.let { Dispatchers.setMain(it) }
        if (setupLiveData) instantTaskExecutorRuleStart()
    }

    protected fun tearDown() {
        dispatcher?.let {
            Dispatchers.resetMain()
            testDispatcher?.cleanupTestCoroutines()
        }
        if (setupLiveData) instantTaskExecutorRuleFinish()
    }
}

/** Has to be setup in the companion Object. */
@ExperimentalCoroutinesApi
data class CustomizableAllTestSetupExtension(override val dispatcher: CoroutineDispatcher? = null,
                                             private val setupLiveData: Boolean = false,
                                             private val setupOnceForAllNested: Boolean = true)
    : BaseCustomizableTestSetupExtension(dispatcher, setupLiveData), BeforeAllCallback, AfterAllCallback {

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
data class CustomizableEachTestSetupExtension(override val dispatcher: CoroutineDispatcher? = null,
                                              private val setupLiveData: Boolean = false)
    : BaseCustomizableTestSetupExtension(dispatcher, setupLiveData), BeforeEachCallback, AfterEachCallback {

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
fun CustomizableAllTestSetupExtension.setupCoroutines(dispatcher: CoroutineDispatcher = TestCoroutineDispatcher())
        : CustomizableAllTestSetupExtension = copy(dispatcher = dispatcher)

@ExperimentalCoroutinesApi
fun CustomizableAllTestSetupExtension.setupLiveData(): CustomizableAllTestSetupExtension = copy(setupLiveData = true)

@ExperimentalCoroutinesApi
fun CustomizableAllTestSetupExtension.setupOnceForAllNested(): CustomizableAllTestSetupExtension = copy(setupOnceForAllNested = true)

@ExperimentalCoroutinesApi
fun CustomizableAllTestSetupExtension.setupSeparateForEachNested(): CustomizableAllTestSetupExtension = copy(setupOnceForAllNested = false)

@ExperimentalCoroutinesApi
fun CustomizableAllTestSetupExtension.setupAll(dispatcher: CoroutineDispatcher? = null): CustomizableAllTestSetupExtension =
    copy(dispatcher = dispatcher, setupLiveData = true, setupOnceForAllNested = true)
// endregion BeforeAllTestSetupExtension builder functions.

// region BeforeEachTestSetupExtension builder functions.
@ExperimentalCoroutinesApi
fun CustomizableEachTestSetupExtension.setupCoroutines(dispatcher: CoroutineDispatcher = TestCoroutineDispatcher())
        : CustomizableEachTestSetupExtension = copy(dispatcher = dispatcher)

@ExperimentalCoroutinesApi
fun CustomizableEachTestSetupExtension.setupLiveData(): CustomizableEachTestSetupExtension = copy(setupLiveData = true)
// endregion BeforeEachTestSetupExtension builder functions.
