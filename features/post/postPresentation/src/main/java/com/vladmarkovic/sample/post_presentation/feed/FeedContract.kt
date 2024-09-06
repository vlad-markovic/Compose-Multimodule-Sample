package com.vladmarkovic.sample.post_presentation.feed

import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.mv.state.StateReducer
import com.vladmarkovic.sample.common.navigation.tab.model.Tab
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_domain.model.DataSource

sealed class FeedState {
    companion object{
        val initial: FeedState = Loading
    }
    data object Loading : FeedState()
    data object Error : FeedState()
    data class Posts(val posts: List<Post>) : FeedState()
}

sealed class FeedChange {
    data object ShowLoading : FeedChange()
    data object ShowError : FeedChange()
    data class ShowPosts(val posts: List<Post>) : FeedChange()
}

sealed class FeedEvent {
    data class OnRefreshPosts(val forceFetch: DataSource = DataSource.UNSPECIFIED) : FeedEvent()
    data class OnPostTapped(val post: Post) : FeedEvent()
    data class OnTabTapped(val tab: Tab<*>) : FeedEvent()
    data class OnShowToast(val message: String) : FeedEvent()
    data object OnOpenDrawer : FeedEvent()
    data object OnCloseDrawer : FeedEvent()
    data object OnSettingsTapped : FeedEvent()
}

sealed class FeedAction : Action {

}

class FeedReducer : StateReducer<FeedState, FeedChange> {
    override fun invoke(state: FeedState, change: FeedChange): FeedState = when(change) {
        is FeedChange.ShowLoading -> FeedState.Loading
        is FeedChange.ShowError -> FeedState.Error
        is FeedChange.ShowPosts -> FeedState.Posts(change.posts)
    }
}
