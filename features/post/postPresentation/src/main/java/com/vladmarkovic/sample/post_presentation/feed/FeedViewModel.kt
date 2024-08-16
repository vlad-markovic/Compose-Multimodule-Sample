/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.feed

import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.common.logging.Lumber
import com.vladmarkovic.sample.common.mvi.MviViewModel
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.CloseDrawer
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.CommonNavigationAction.OpenDrawer
import com.vladmarkovic.sample.common.navigation.tab.navcomponent.model.ToTab
import com.vladmarkovic.sample.core.coroutines.collectIn
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_presentation.navigation.ToPostScreen
import com.vladmarkovic.sample.shared_domain.connectivity.NetworkConnectivity
import com.vladmarkovic.sample.shared_domain.model.DataSource
import com.vladmarkovic.sample.shared_presentation.display.CommonDisplayAction
import com.vladmarkovic.sample.shared_presentation.navigation.ToSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    connection: NetworkConnectivity
): MviViewModel<FeedState, FeedChange, FeedAction, FeedEvent>(
    FeedState.initial, FeedReducer()
) {

    init {
        connection.connectionState.collectIn(viewModelScope) { connected ->
            if (connected && state.value is FeedState.Error) {
                onEvent(FeedEvent.OnRefreshPosts(DataSource.REMOTE))
            }
        }

        postRepository.observePostsCache().collectIn(viewModelScope) { posts ->
            changeState(FeedChange.ShowPosts(posts))
        }

        onEvent(FeedEvent.OnRefreshPosts())
    }

    override fun onEvent(event: FeedEvent) {
        when (event) {
            is FeedEvent.OnRefreshPosts -> refreshPosts(event.forceFetch)
            is FeedEvent.OnPostTapped -> navigate(ToPostScreen(event.post))
            is FeedEvent.OnTabTapped -> navigate(ToTab(event.tab))
            is FeedEvent.OnSettingsTapped -> navigate(ToSettings)
            is FeedEvent.OnOpenDrawer -> navigate(OpenDrawer)
            is FeedEvent.OnCloseDrawer -> navigate(CloseDrawer)
            is FeedEvent.OnShowToast -> display(CommonDisplayAction.Toast(event.message))
        }
    }

    private fun refreshPosts(forceFetch: DataSource) {
        viewModelScope.launch {
            changeState(FeedChange.ShowLoading)

            if (forceFetch == DataSource.CACHE) {
                changeState(FeedChange.ShowPosts(emptyList()))
            }

            val (posts, error) = try {
                Pair(postRepository.fetchAllPosts(forceFetch), null)
            } catch (exception: Exception) {
                Lumber.e(exception, "Error fetching posts")
                when (exception) {
                    is UnresolvedAddressException, is IOException -> {
                        val cachedPosts = postRepository.fetchAllPosts(DataSource.CACHE)
                        if (cachedPosts.isEmpty()) Pair(null, true)
                        else Pair(cachedPosts, null)
                    }
                    else -> {
                        throw exception
                    }
                }
            }

            posts?.let { changeState(FeedChange.ShowPosts(posts)) }
            error?.let { changeState(FeedChange.ShowError) }
        }
    }
}
