package com.vladmarkovic.sample.shared_presentation.util

import android.app.Activity
import android.widget.FrameLayout
import com.vladmarkovic.sample.shared_presentation.R
import com.vladmarkovic.sample.shared_presentation.navigation.Navigation

/** Helper function for navigating from [Activity] with `navigate { somewhere() }`. */
inline fun Activity.navigate(crossinline destination: Navigation.() -> Unit) {
    navigation.destination()
}

/** Set simple FrameLayout view for a container Activity which simply holds Fragments. */
fun Activity.setContainerContentView() {
    setContentView(FrameLayout(this).apply {
        val matchParent = FrameLayout.LayoutParams.MATCH_PARENT
        layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)
        id = R.id.container
    })
}
