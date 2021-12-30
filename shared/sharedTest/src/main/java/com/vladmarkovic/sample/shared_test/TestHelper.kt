package com.vladmarkovic.sample.shared_test

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
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
 * - LiveData
 *
 * Use example:
 *  @ExtendWith(SomeTest.EachTestSetupExtension::class)
 *  internal class SomeTest { ...
 */
class EachTestSetupExtension : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        instantTaskExecutorRuleStart()
    }

    override fun afterEach(context: ExtensionContext?) {
        instantTaskExecutorRuleFinish()
    }
}

/**
 * Use example:
 *  @ExtendWith(FullTestSetupExtension::class)
 *  internal class SomeTest { ...
 */
class AllTestSetupExtension : BeforeAllCallback, AfterAllCallback {

    override fun beforeAll(context: ExtensionContext?) {
        // Because beforeAll/afterAll are called for each @Nested inner test class, we need to first resetAll().
        resetAll()
        instantTaskExecutorRuleStart()
    }

    override fun afterAll(context: ExtensionContext?) {
        resetAll()
    }

    private fun resetAll() {
        instantTaskExecutorRuleFinish()
    }
}
