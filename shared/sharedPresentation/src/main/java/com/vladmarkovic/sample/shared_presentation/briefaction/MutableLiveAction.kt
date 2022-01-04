/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.briefaction

class MutableLiveAction<A : BriefAction>(
    allowMultipleObservers: Boolean = false
) : LiveAction<A>(allowMultipleObservers) {

    public override fun setValue(action: A?) = super.setValue(action)
}
