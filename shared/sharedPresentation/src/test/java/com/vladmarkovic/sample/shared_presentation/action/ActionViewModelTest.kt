package com.vladmarkovic.sample.shared_presentation.action

import com.vladmarkovic.sample.common.mv.action.ActionViewModel
import com.vladmarkovic.sample.common.mv.action.NavigationAction
import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.mv.action.action
import com.vladmarkovic.sample.common.mv.action.navigate
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.CommonNavigationAction
import com.vladmarkovic.sample.shared_test.AllTestSetupExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/** Test actioning [Action]s like [NavigationAction]s via [ActionViewModel] */
@ExperimentalCoroutinesApi
@ExtendWith(AllTestSetupExtension::class)
class ActionViewModelTest {

    companion object {
        @JvmStatic
        @Suppress("Unused")
        fun actionArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(CommonNavigationAction.Back)
        )

        @JvmStatic
        @Suppress("Unused")
        fun navArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(CommonNavigationAction.Back)
        )
    }

    private val viewModel = ActionViewModel()

    @MethodSource("actionArgs")
    @DisplayName("Given actioning via ActionViewModel")
    @ParameterizedTest(name = "When ''{0}'' action is sent, Then ''{0}'' action is received")
    fun testActionViewModelActions(action: Action) {
        viewModel.action(action)

        // FIXME test viewModel.actioner.action.assertValueEquals(action)
    }

    @MethodSource("navArgs")
    @DisplayName("Given navigating via ActionViewModel")
    @ParameterizedTest(name = "When ''{0}'' navigation action is sent, Then it navigates to ''{0}''")
    fun testCommonNavigationActions(action: NavigationAction) {
        viewModel.navigate(action)

        // FIXME test viewModel.actioner.action.assertValueEquals(action)
    }
}
