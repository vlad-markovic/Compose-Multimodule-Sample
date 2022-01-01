package com.vladmarkovic.sample.shared_presentation.briefaction

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_test.AllTestSetupExtension
import com.vladmarkovic.sample.shared_test.assertValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/** Test actioning [BriefAction]s like [NavigationAction]s via [BriefActionViewModel] */
@ExperimentalCoroutinesApi
@ExtendWith(AllTestSetupExtension::class)
class BriefActionViewModelTest {

    companion object {
        object ToSomeScreen : NavigationAction

        @JvmStatic
        @Suppress("Unused")
        fun actionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(ToSomeScreen)
        )

        @JvmStatic
        @Suppress("Unused")
        fun navArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(ToSomeScreen)
        )
    }

    private val viewModel = BriefActionViewModel()

    @MethodSource("actionArgs")
    @DisplayName("Given actioning via BriefActionViewModel")
    @ParameterizedTest(name = "When ''{0}'' action is sent, Then ''{0}'' action is received")
    fun testBriefActionViewModelActions(action: BriefAction) {
        viewModel.action(action)

        viewModel.action.assertValueEquals(action)
    }

    @MethodSource("navArgs")
    @DisplayName("Given navigating via BriefActionViewModel")
    @ParameterizedTest(name = "When ''{0}'' navigation action is sent, Then it navigates to ''{0}''")
    fun testCommonNavigationActions(action: NavigationAction) {
        viewModel.navigate(action)

        viewModel.action.assertValueEquals(action)
    }
}
