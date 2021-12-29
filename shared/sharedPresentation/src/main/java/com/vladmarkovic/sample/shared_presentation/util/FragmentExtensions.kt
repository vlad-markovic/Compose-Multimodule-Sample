import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

/** Utility returning View as ComposeView for onCreateView. */
fun Fragment.composeContent(content: @Composable () -> Unit): ComposeView =
    ComposeView(requireContext()).apply { setContent(content) }
