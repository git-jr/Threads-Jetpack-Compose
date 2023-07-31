package com.paradoxo.threadscompose.ui.feed

import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.model.UserAccount

data class FeedScreenState(
    var posts: List<Post> = emptyList(),
    val currentUserProfile: UserAccount = UserAccount(),
)
